package pl.oskarinio.inpoint.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.oskarinio.inpoint.integration.model.ParcelLocker;
import pl.oskarinio.inpoint.integration.model.ParcelLockersResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParcelLockerFetchingService {

    private final WebClient webClient;

    @Value("${inpost.api.url}")
    private String apiUrl;
    @Value("${inpost.api.page-size:1000}")
    private int pageSize;
    @Value("${inpost.api.threads.amount:50}")
    private int threadsAmount;

    private volatile List<ParcelLocker> cachedLockers = Collections.emptyList();

    @EventListener(ApplicationReadyEvent.class)
    private void onApplicationReady() {
        refreshCache();
    }

    private void refreshCache() {
        log.info("Start downloading parcel lockers data...");
        long startTime = System.currentTimeMillis();

        fetchFirstPage()
                .flatMapMany(firstPage -> {
                    Flux<ParcelLocker> firstPageStream = Flux.fromIterable(firstPage.getItems());
                    Flux<ParcelLocker> remainingStream = fetchRemainingPages(firstPage.getTotalPages());
                    return Flux.concat(firstPageStream, remainingStream);
                })
                .collectList()
                .doOnSuccess(parcelLockers -> handleSuccess(parcelLockers, startTime))
                .doOnError(error -> log.error("Critical error: {}", error.getMessage()))
                .subscribe();
    }

    private Mono<ParcelLockersResponse> fetchFirstPage() {
        return fetchSinglePage(1);
    }

    private Flux<ParcelLocker> fetchRemainingPages(int totalPages) {
        if (totalPages <= 1){
            return Flux.empty();
        }

        return Flux.range(2, totalPages - 1)
                .flatMap(pageNumber -> fetchSinglePage(pageNumber)
                        .flatMapIterable(ParcelLockersResponse::getItems)
                        .onErrorResume(error -> handlePageError(pageNumber, error)), threadsAmount);
    }

    private Mono<ParcelLockersResponse> fetchSinglePage(int pageNumber) {
        return webClient.get()
                .uri(apiUrl, uriBuilder -> uriBuilder
                        .queryParam("page", pageNumber)
                        .queryParam("per_page", pageSize)
                        .build())
                .retrieve()
                .bodyToMono(ParcelLockersResponse.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)));
    }

    private void handleSuccess(List<ParcelLocker> parcelLockers, long startTime) {
        if (parcelLockers == null || parcelLockers.isEmpty()) {
            log.warn("Downloaded list is empty. Cache not updated.");
            return;
        }

        this.cachedLockers = List.copyOf(parcelLockers);
        log.info("Cache ready! {} points downloaded in {} ms",
                cachedLockers.size(), System.currentTimeMillis() - startTime);
    }

    private Flux<ParcelLocker> handlePageError(int pageNumber, Throwable e) {
        log.warn("Failed to fetch page {}: {}", pageNumber, e.getMessage());
        return Flux.empty();
    }

    public List<ParcelLocker> getAllLockersAsList() {
        return this.cachedLockers;
    }
}