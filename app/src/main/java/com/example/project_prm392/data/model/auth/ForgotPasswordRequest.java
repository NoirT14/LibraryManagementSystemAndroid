package com.example.project_prm392.data.model.auth;

public class ForgotPasswordRequest {
    private String usernameorEmail;

    public ForgotPasswordRequest(String usernameorEmail) {
        this.usernameorEmail = usernameorEmail;
    }

    public String getUsernameorEmail() { return usernameorEmail; }
    public void setUsernameorEmail(String usernameorEmail) { this.usernameorEmail = usernameorEmail; }
}
