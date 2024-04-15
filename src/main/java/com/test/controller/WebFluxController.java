package com.test.controller;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@RestController
@Data
@RequestMapping("api/flux")
@RequiredArgsConstructor
public class WebFluxController {
    private String str;
    private List<String> live=new ArrayList<>();

    @GetMapping(value = "get", produces = MediaType.TEXT_EVENT_STREAM_VALUE+ ";charset=utf-8")
    public Mono<ResponseEntity<String>> getString() {
        return Mono.just(ResponseEntity.ok("mono " + str));
    }

    @GetMapping(path = "/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE+ ";charset=utf-8")
    public Flux<ServerSentEvent<Object>> consumer() {
        return Flux.create(a-> {
            live.add(UUID.randomUUID().toString());
        }).map(
                liveScore -> ServerSentEvent.builder().data(liveScore).event("goal").build());
    }

    @GetMapping("edit")
    public String setString(@RequestParam String strr) {
        this.setStr(strr);
        live.add(UUID.randomUUID().toString());
        return "done";
    }
}
