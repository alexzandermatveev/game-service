package com.example.game_service.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document("user_activity")
public class UserActivity {
    @Id
    private String id;

    private String userId;
    private int activity;
    private Instant timestamp;

    public UserActivity(String userId, int activity) {
        this.userId = userId;
        this.activity = activity;
        this.timestamp = Instant.now();
    }
}
