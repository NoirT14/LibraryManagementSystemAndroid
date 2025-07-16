package com.example.project_prm392.data.model.auth;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private BrowserInfo browserInfo;

    public RegisterRequest(String username, String email, String password, String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.browserInfo = BrowserInfo.createDefault();
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BrowserInfo getBrowserInfo() { return browserInfo; }
    public void setBrowserInfo(BrowserInfo browserInfo) { this.browserInfo = browserInfo; }
}
