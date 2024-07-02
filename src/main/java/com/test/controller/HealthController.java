package com.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class HealthController {
    private Boolean HEALTH_UP = true;

    @GetMapping("customhealth")
    public ResponseEntity<?> healthTest() {
        if (HEALTH_UP)
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("change")
    public ResponseEntity<?> changeHealth() {
        HEALTH_UP = !HEALTH_UP;
        return ResponseEntity.ok().build();
    }
}
