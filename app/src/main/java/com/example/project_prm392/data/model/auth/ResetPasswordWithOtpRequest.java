package com.example.project_prm392.data.model.auth;

public class ResetPasswordWithOtpRequest {
    private String email;
    private String otpCode;
    private String newPassword;
    private String confirmPassword;

    public ResetPasswordWithOtpRequest(String email, String otpCode, String newPassword, String confirmPassword) {
        this.email = email;
        this.otpCode = otpCode;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
