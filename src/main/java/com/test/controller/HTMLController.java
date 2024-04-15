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
        String output = service.convert(payload.html());
        var pc = SalesForceConfig.buildSalesforcePartnerConnection(payload.accessToken(), payload.instanceUrl() + "/services/Soap/u/59.0");
        return ResponseEntity.ok().body(service.upload(pc, payload.parentId(), output));
    }
}
