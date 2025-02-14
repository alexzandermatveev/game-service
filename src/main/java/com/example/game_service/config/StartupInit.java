package com.example.game_service.config;

import com.example.game_service.entities.UserDoc;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StartupInit {
    private final ReactiveMongoTemplate mongoTemplate;

    @PostConstruct
    public void init() {
        mongoTemplate.collectionExists("users_local")
                .flatMap(exists -> exists ? Mono.empty() : mongoTemplate.createCollection("users_local"))
                .block();

        mongoTemplate.collectionExists("user_activity")
                .flatMap(exists -> exists ? Mono.empty() : mongoTemplate.createCollection("user_activity"))
                .block();

        // Добавляем тестового пользователя, если коллекция пуста
        mongoTemplate.findById("123", UserDoc.class)
                .switchIfEmpty(
                        mongoTemplate.insert(new UserDoc("123", 1000, "RU", Instant.now(), Map.of("level", 10)))
                )
                .block();
    }
}
