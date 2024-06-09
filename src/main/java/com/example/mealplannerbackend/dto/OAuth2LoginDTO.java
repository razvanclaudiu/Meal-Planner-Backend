package com.example.mealplannerbackend.dto;

public class OAuth2LoginDTO {
    private String token;

    public OAuth2LoginDTO() {}

    public OAuth2LoginDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
