package com.example.game_service.services;

import com.example.game_service.entities.UserActivity;
import com.example.game_service.repositories.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final UserActivityRepository userActivityRepository;


    @KafkaListener(topics = "user_activity", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeActivity(String message) {
        // Разбираем сообщение
        String[] parts = message.split(":");
        if (parts.length != 2) return;

        String userId = parts[0];
        int activity = Integer.parseInt(parts[1]);

        UserActivity userActivity = new UserActivity(userId, activity);
        userActivityRepository.save(userActivity).subscribe();
    }
}
