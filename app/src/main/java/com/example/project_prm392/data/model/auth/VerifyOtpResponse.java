package com.example.project_prm392.data.model.auth;

public class VerifyOtpResponse {
    private String message;
    private boolean isSuccess;
    private VerifyOtpData data;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return isSuccess; }
    public void setIsSuccess(boolean isSuccess) { this.isSuccess = isSuccess; }

    public VerifyOtpData getData() { return data; }
    public void setData(VerifyOtpData data) { this.data = data; }

    public static class VerifyOtpData {
        private String email;
        private int userId;
        private String message;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
