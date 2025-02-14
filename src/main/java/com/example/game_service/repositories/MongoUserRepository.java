package com.example.game_service.repositories;

import com.example.game_service.entities.UserDoc;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public interface MongoUserRepository extends ReactiveMongoRepository<UserDoc, String> {

    Flux<UserDoc> findTop10ByCountryOrderByMoneyDesc(String country);
    Mono<Long> countByCountryAndCreatedAtBetween(String country, Instant start, Instant end);

}
