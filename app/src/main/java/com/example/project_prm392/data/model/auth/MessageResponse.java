package com.example.project_prm392.data.model.auth;

public class MessageResponse {
    private String message;
    private boolean isSuccess;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return isSuccess; }
    public void setSuccess(boolean success) { isSuccess = success; }
}
