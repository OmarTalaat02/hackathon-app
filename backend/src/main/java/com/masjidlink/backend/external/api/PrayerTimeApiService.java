package com.masjidlink.backend.external.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient; // Import WebClient

import reactor.core.publisher.Mono; // Import Mono for reactive types

@Service
public class PrayerTimeApiService {

    private final WebClient webClient;

    // Base URL for the AlAdhan API (can be externalized to application.properties)
    @Value("${api.aladhan.base-url:https://api.aladhan.com/v1}")
    private String alAdhanApiBaseUrl;

    public PrayerTimeApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(alAdhanApiBaseUrl).build();
    }

    /**
     * Fetches prayer times for a given date, latitude, longitude, and method.
     * Method 2 (ISNA) is commonly used.
     * See https://aladhan.com/prayer-times-api for details.
     * @param date e.g., "28-06-2025"
     * @param latitude
     * @param longitude
     * @param method Calculation method (e.g., 2 for ISNA)
     * @return Mono<String> containing the JSON response from the API
     */
    public Mono<String> getPrayerTimes(String date, double latitude, double longitude, int method) {
        String uri = String.format("%s/timings/%s?latitude=%f&longitude=%f&method=%d",
                alAdhanApiBaseUrl, date, latitude, longitude, method);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * Fetches prayer times for a given month, year, latitude, longitude, and method.
     * @param year
     * @param month
     * @param latitude
     * @param longitude
     * @param method Calculation method (e.g., 2 for ISNA)
     * @return Mono<String> containing the JSON response from the API
     */
    public Mono<String> getPrayerTimesForMonth(int year, int month, double latitude, double longitude, int method) {
        String uri = String.format("%s/calendar/%d/%d?latitude=%f&longitude=%f&method=%d",
                alAdhanApiBaseUrl, year, month, latitude, longitude, method);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class);
    }
}