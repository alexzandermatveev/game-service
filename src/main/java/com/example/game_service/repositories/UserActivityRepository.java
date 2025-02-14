package com.example.game_service.repositories;

import com.example.game_service.entities.UserActivity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Instant;

@Repository
public interface UserActivityRepository extends ReactiveMongoRepository<UserActivity, String> {

    Flux<UserActivity> findByUserIdAndTimestampBetween(String userId, Instant start, Instant end);
}
