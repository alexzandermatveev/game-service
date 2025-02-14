package com.example.game_service.services;

import com.example.game_service.entities.UserDoc;
import com.example.game_service.repositories.MongoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MongoUserRepository userRepository;
    private final ReactiveRedisOperations<String, UserDoc> redisOperations;


    @Value("${cache.ttl}")
    private long cacheTtl;

    /**
     * Синхронизация данных пользователя
     */
    public Mono<UserDoc> syncUserData(String userId, UserDoc newData) {
        return userRepository.findById(userId)
                .flatMap(existingUser -> {
                    existingUser.setMoney(newData.getMoney());
                    existingUser.setAdditionalData(newData.getAdditionalData());
                    return userRepository.save(existingUser);
                })
                .switchIfEmpty(userRepository.save(newData))
                .flatMap(savedUser -> redisOperations.opsForValue()
                        .set(userId, savedUser, Duration.ofSeconds(cacheTtl))
                        .thenReturn(savedUser));
    }

    /**
     * Получение сохраненных данных пользователя
     */
    public Mono<UserDoc> getUserData(String userId) {
        return redisOperations.opsForValue()
                .get(userId)
                .switchIfEmpty(
                        userRepository.findById(userId)
                                .flatMap(user ->
                                        redisOperations.opsForValue()
                                                .set(userId, user, Duration.ofSeconds(cacheTtl))
                                                .thenReturn(user)
                                )
                                .defaultIfEmpty(new UserDoc(userId, 0, "Unknown", Instant.now(), Map.of())) // Возвращаем дефолтный объект, если пользователь не найден
                );
    }
}
