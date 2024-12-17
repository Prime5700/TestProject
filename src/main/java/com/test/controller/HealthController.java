package com.test.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/health")
@RequiredArgsConstructor
public class HealthController {
    private Boolean HEALTH_UP = true;
    private Integer COUNT = 0;
    private final static String customString;

    static {
        customString = RandomStringUtils.randomAlphabetic(10);
    }

    @GetMapping("change")
    public ResponseEntity<?> changeHealth() {
        HEALTH_UP = !HEALTH_UP;
        return ResponseEntity.ok().build();
    }

    @GetMapping("test")
    public ResponseEntity<?> currentHealth() {
        if (HEALTH_UP)
            return ResponseEntity.ok(++COUNT + " " + customString);
        return ResponseEntity.badRequest().body(++COUNT + " " + customString);
    }
}
