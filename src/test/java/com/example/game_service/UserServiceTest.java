package com.example.game_service;

import com.example.game_service.entities.UserDoc;
import com.example.game_service.repositories.MongoUserRepository;
import com.example.game_service.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private MongoUserRepository userRepository;

    @Mock
    private ReactiveRedisOperations<String, UserDoc> redisOperations;

    @Mock
    private ReactiveValueOperations<String, UserDoc> valueOperations;

    @InjectMocks
    private UserService userService;

    private UserDoc testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserDoc();
        testUser.setId("123");
        testUser.setMoney(1000);
        testUser.setCountry("RU");
        testUser.setCreatedAt(Instant.now());
        testUser.setAdditionalData(Map.of("level", 10));

        when(redisOperations.opsForValue()).thenReturn(valueOperations);

        when(valueOperations.get(anyString())).thenReturn(Mono.empty());

        when(userRepository.findById(anyString())).thenReturn(Mono.empty());
    }

    @Test
    void testGetUserFromRedis() {
        when(valueOperations.get("123")).thenReturn(Mono.just(testUser));

        StepVerifier.create(userService.getUserData("123"))
                .expectNext(testUser)
                .verifyComplete();

        verify(valueOperations).get("123");

//        verify(userRepository, never()).findById(anyString());
    }



    @Test
    void testGetUserFromMongoIfNotInRedis() {
        when(valueOperations.get("123")).thenReturn(Mono.empty());
        when(userRepository.findById("123")).thenReturn(Mono.just(testUser));

        // Гарантируем, что `set` возвращает Mono.just(true), а не null
        when(valueOperations.set(eq("123"), any(UserDoc.class), any(Duration.class)))
                .thenReturn(Mono.just(true));

        StepVerifier.create(userService.getUserData("123"))
                .expectNext(testUser)
                .verifyComplete();

        verify(userRepository).findById("123");
        verify(valueOperations, atLeastOnce()).set(eq("123"), any(UserDoc.class), any(Duration.class));
    }


    @Test
    void testSyncUserData() {
        when(userRepository.findById("123")).thenReturn(Mono.just(testUser));
        when(userRepository.save(any(UserDoc.class))).thenReturn(Mono.just(testUser));
        when(valueOperations.set(eq("123"), any(UserDoc.class), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(userService.syncUserData("123", testUser))
                .expectNext(testUser)
                .verifyComplete();

        verify(userRepository, atMost(2)).save(testUser);
    }

}
