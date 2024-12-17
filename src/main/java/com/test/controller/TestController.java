package com.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("hi")
    public ResponseEntity<?> hi() {
        return ResponseEntity.ok("Hi from body");
    }
}
