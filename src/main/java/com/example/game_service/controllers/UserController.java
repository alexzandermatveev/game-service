package com.example.game_service.controllers;

import com.example.game_service.entities.UserDoc;
import com.example.game_service.services.ActivityProducer;
import com.example.game_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ActivityProducer activityProducer;


    /**
     * Прием данных синхронизации от пользователя
     */
    @PostMapping("/{userId}")
    public Mono<UserDoc> syncUser(@PathVariable String userId, @RequestBody UserDoc userData) {
        return userService.syncUserData(userId, userData);
    }

    /**
     * Отправка данных пользователю
     */
    @GetMapping("/{userId}")
    public Mono<UserDoc> getUser(@PathVariable String userId) {
        return userService.getUserData(userId);
    }

    /**
     * Отправка активности пользователя в Kafka
     */
    @PostMapping("/activity/{userId}")
    public Mono<Void> sendActivity(@PathVariable String userId, @RequestParam int activity) {
        activityProducer.sendActivity(userId, activity);
        return Mono.empty();
    }
}

