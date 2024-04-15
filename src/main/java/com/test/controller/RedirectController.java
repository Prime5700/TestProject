package com.test.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class RedirectController {

    @GetMapping("/redirect")
    public ResponseEntity<Void> redirectToAnotherPage(HttpServletResponse response) {
        URI location = URI.create("https://example.com/destination");
        return ResponseEntity.status(302)
                .location(location)
                .build();
    }

    @GetMapping("/redirect-with-uri-builder")
    public ResponseEntity<Void> redirectToAnotherPageWithUriBuilder() {
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/redirect")
                .build()
                .toUri();
        return ResponseEntity.status(302)
                .location(location)
                .build();
    }
}
