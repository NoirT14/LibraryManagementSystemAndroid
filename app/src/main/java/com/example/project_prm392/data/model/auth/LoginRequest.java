package com.example.project_prm392.data.model.auth;

public class LoginRequest {
    private String usernameorEmail;
    private String password;
    private boolean rememberMe;
    private BrowserInfo browserInfo;

    public LoginRequest(String usernameorEmail, String password, boolean rememberMe) {
        this.usernameorEmail = usernameorEmail;
        this.password = password;
        this.rememberMe = rememberMe;
        this.browserInfo = BrowserInfo.createDefault();
    }

    // Getters and Setters
    public String getUsernameorEmail() { return usernameorEmail; }
    public void setUsernameorEmail(String usernameorEmail) { this.usernameorEmail = usernameorEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isRememberMe() { return rememberMe; }
    public void setRememberMe(boolean rememberMe) { this.rememberMe = rememberMe; }

    public BrowserInfo getBrowserInfo() { return browserInfo; }
    public void setBrowserInfo(BrowserInfo browserInfo) { this.browserInfo = browserInfo; }
}
