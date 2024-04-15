package com.test.payload.request;

public record HTMLPayload(String accessToken, String instanceUrl, String html, String parentId) {
}
