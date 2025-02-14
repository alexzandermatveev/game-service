package com.example.game_service;

import com.example.game_service.entities.UserDoc;
import com.example.game_service.repositories.MongoUserRepository;
import com.example.game_service.services.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

class AnalyticsServiceTest {

    @Mock
    private MongoUserRepository userRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    private UserDoc user1, user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new UserDoc();
        user1.setId("1");
        user1.setMoney(2000);
        user1.setCountry("RU");
        user1.setCreatedAt(Instant.now());

        user2 = new UserDoc();
        user2.setId("2");
        user2.setMoney(1500);
        user2.setCountry("RU");
        user2.setCreatedAt(Instant.now());
    }

    @Test
    void testGetTopUsersByMoney() {
        when(userRepository.findTop10ByCountryOrderByMoneyDesc("RU"))
                .thenReturn(Flux.fromIterable(List.of(user1, user2)));

        StepVerifier.create(analyticsService.getTopUsersByMoney("RU", 2))
                .expectNext(user1, user2)
                .verifyComplete();

        verify(userRepository).findTop10ByCountryOrderByMoneyDesc("RU");
    }

    @Test
    void testCountNewUsers() {
        Instant start = Instant.now().minusSeconds(3600);
        Instant end = Instant.now();
        when(userRepository.countByCountryAndCreatedAtBetween("RU", start, end)).thenReturn(Mono.just(5L));

        StepVerifier.create(analyticsService.countNewUsers("RU", start, end))
                .expectNext(5L)
                .verifyComplete();

        verify(userRepository).countByCountryAndCreatedAtBetween("RU", start, end);
    }
}
