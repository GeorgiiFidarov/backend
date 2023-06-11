package org.greenway.backend.controller;

import java.util.Map;

public class LoginApiResponse {
    private String status;
    private String message;
    private String accessToken;
    private Map<String,String> userInfo;

    public LoginApiResponse(String status, String message, String accessToken, Map<String, String> userInfo) {
        this.status = status;
        this.message = message;
        this.accessToken = accessToken;
        this.userInfo = userInfo;
    }

    public LoginApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public LoginApiResponse(String status, String message, String accessToken) {
        this.status = status;
        this.message = message;
        this.accessToken = accessToken;
    }
    public Map<String, String> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Map<String, String> userInfo) {
        this.userInfo = userInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
