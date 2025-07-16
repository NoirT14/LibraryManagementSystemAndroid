package com.example.project_prm392.data.model.auth;

public class AuthResult {
    private boolean isSuccess;
    private String errorMessage;
    private LoginResponse data;

    // Getters and Setters
    public boolean isSuccess() { return isSuccess; }
    public void setSuccess(boolean success) { isSuccess = success; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public LoginResponse getData() { return data; }
    public void setData(LoginResponse data) { this.data = data; }
}
