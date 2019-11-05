package com.quemepongo.api.dto;

public class AuthorizationResponse {
    private String authToken;

    public AuthorizationResponse(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
