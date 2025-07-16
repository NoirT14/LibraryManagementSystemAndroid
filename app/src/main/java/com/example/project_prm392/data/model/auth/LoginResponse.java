package com.example.project_prm392.data.model.auth;

public class LoginResponse {
    private String token;
    private int role;
    private SessionInfo sessionInfo;

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public SessionInfo getSessionInfo() { return sessionInfo; }
    public void setSessionInfo(SessionInfo sessionInfo) { this.sessionInfo = sessionInfo; }
}
