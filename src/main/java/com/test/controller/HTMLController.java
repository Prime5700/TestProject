package com.test.controller;

import com.sforce.ws.ConnectionException;
import com.test.config.SalesForceConfig;
import com.test.payload.request.HTMLPayload;
import com.test.service.HTMLService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/html")
@Log4j2
@RequiredArgsConstructor
public class HTMLController {
    private final HTMLService service;

    @PostMapping("pdf")
    public ResponseEntity<String> convert(@RequestBody HTMLPayload payload) throws IOException, ConnectionException {
        log.info(payload);
        if(payload==null) return ResponseEntity.badRequest().body("No payload received");
        if(payload.html()==null|| payload.html().isBlank()) return ResponseEntity.badRequest().body("HTML is null or empty");
        if(payload.accessToken()==null|| payload.accessToken().isBlank()) return ResponseEntity.badRequest().body("AccessToken is null or empty");
        if(payload.parentId()==null|| payload.parentId().isBlank()) return ResponseEntity.badRequest().body("ParentId is null or empty");
        if(payload.instanceUrl()==null|| payload.instanceUrl().isBlank()) return ResponseEntity.badRequest().body("Instance url is null or empty");
        String output = service.convert(payload.html());
        var pc = SalesForceConfig.buildSalesforcePartnerConnection(payload.accessToken(), payload.instanceUrl() + "/services/Soap/u/59.0");
        return ResponseEntity.ok().body(service.upload(pc, payload.parentId(), output));
    }
}
