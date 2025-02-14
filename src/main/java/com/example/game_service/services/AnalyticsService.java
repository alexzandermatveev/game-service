package com.example.game_service.services;


import com.example.game_service.entities.UserActivity;
import com.example.game_service.entities.UserDoc;
import com.example.game_service.repositories.MongoUserRepository;
import com.example.game_service.repositories.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final MongoUserRepository userRepository;
    private final UserActivityRepository userActivityRepository;

    /**
     * Топ X пользователей по деньгам в стране
     */
    public Flux<UserDoc> getTopUsersByMoney(String country, int limit) {
        return userRepository.findTop10ByCountryOrderByMoneyDesc(country).take(limit);
    }

    /**
     * Подсчет новых пользователей за период
     */
    public Mono<Long> countNewUsers(String country, Instant start, Instant end) {
        return userRepository.countByCountryAndCreatedAtBetween(country, start, end);
    }

    /**
     * Список активности пользователя за период
     */
    public Flux<UserActivity> getUserActivity(String userId, Instant start, Instant end) {
        return userActivityRepository.findByUserIdAndTimestampBetween(userId, start, end);
    }
}
