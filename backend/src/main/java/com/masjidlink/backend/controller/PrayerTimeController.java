package com.masjidlink.backend.controller;

import com.masjidlink.backend.external.api.PrayerTimeApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono; // Import Mono

@RestController
@RequestMapping("/api/v1/prayer-times")
public class PrayerTimeController {

    private final PrayerTimeApiService prayerTimeApiService;

    public PrayerTimeController(PrayerTimeApiService prayerTimeApiService) {
        this.prayerTimeApiService = prayerTimeApiService;
    }

    /**
     * Endpoint to get daily prayer times for a specific location and date.
     * Example: GET /api/v1/prayer-times/daily?date=28-06-2025&latitude=40.895&longitude=-74.009&method=2
     */
    @GetMapping("/daily")
    public Mono<ResponseEntity<String>> getDailyPrayerTimes(
            @RequestParam String date, // Format: DD-MM-YYYY (e.g., 28-06-2025)
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "2") int method) { // Default method 2 (ISNA)

        return prayerTimeApiService.getPrayerTimes(date, latitude, longitude, method)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Handle empty response
    }

    /**
     * Endpoint to get monthly prayer times for a specific location and month/year.
     * Example: GET /api/v1/prayer-times/monthly?year=2025&month=6&latitude=40.895&longitude=-74.009&method=2
     */
    @GetMapping("/monthly")
    public Mono<ResponseEntity<String>> getMonthlyPrayerTimes(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "2") int method) {

        return prayerTimeApiService.getPrayerTimesForMonth(year, month, latitude, longitude, method)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}