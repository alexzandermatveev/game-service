package com.example.game_service;

import com.example.game_service.entities.UserActivity;
import com.example.game_service.repositories.UserActivityRepository;
import com.example.game_service.services.KafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

class ActivityKafkaTest {

    @Mock
    private UserActivityRepository userActivityRepository;

    @InjectMocks
    private KafkaConsumer activityConsumer;

    private final String testMessage = "123:50";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumeActivity() {
        UserActivity mockActivity = new UserActivity("123", 50);
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn(Mono.just(mockActivity));

        activityConsumer.consumeActivity(testMessage);

        verify(userActivityRepository).save(any(UserActivity.class));
    }
}
