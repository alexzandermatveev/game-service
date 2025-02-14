package com.example.game_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "user_activity";

    public void sendActivity(String userId, int activity) {
        String message = userId + ":" + activity;
        kafkaTemplate.send(TOPIC, message);
    }
}
