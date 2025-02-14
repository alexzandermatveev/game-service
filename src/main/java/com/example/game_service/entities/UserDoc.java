package com.example.game_service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@Document("users_local")
public class UserDoc {

    @Id
    private String id;  // UUID пользователя

    private Integer money;
    private String country;
    private Instant createdAt;

    private Map<String, Object> additionalData; // Остальные данные JSON

    public UserDoc() {
        this.createdAt = Instant.now(); // проставляем дату
    }
}
