package com.example.game_service.controllers;


import com.example.game_service.entities.UserActivity;
import com.example.game_service.entities.UserDoc;
import com.example.game_service.services.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * Топ X пользователей по деньгам в стране
     */
    @GetMapping("/top-users")
    public Flux<UserDoc> getTopUsers(@RequestParam String country, @RequestParam int limit) {
        return analyticsService.getTopUsersByMoney(country, limit);
    }

    /**
     * Количество новых пользователей в стране за период
     */
    @GetMapping("/new-users")
    public Mono<Long> countNewUsers(@RequestParam String country,
                                    @RequestParam String startDate,
                                    @RequestParam String endDate) {
        Instant start = Instant.parse(startDate);
        Instant end = Instant.parse(endDate);
        return analyticsService.countNewUsers(country, start, end);
    }

    /**
     * Список активности пользователя за период
     */
    @GetMapping("/activity")
    public Flux<UserActivity> getUserActivity(@RequestParam String userId,
                                              @RequestParam String startDate,
                                              @RequestParam String endDate) {
        Instant start = Instant.parse(startDate);
        Instant end = Instant.parse(endDate);
        return analyticsService.getUserActivity(userId, start, end);
    }
}
