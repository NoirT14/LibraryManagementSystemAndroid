package com.example.project_prm392.utils;

public class Constants {
    public static final String BASE_URL = "http://10.0.2.2:5027/"; // Thay bằng URL thực của bạn

    // API Endpoints
    public static final String LOGIN_ENDPOINT = "api/auth/login";
    public static final String REGISTER_ENDPOINT = "api/auth/register";
    public static final String LOGOUT_ENDPOINT = "api/auth/logout";
    public static final String FORGOT_PASSWORD_ENDPOINT = "api/auth/forgot-password";
    public static final String RESET_PASSWORD_ENDPOINT = "api/auth/reset-password";

    // Request/Response codes
    public static final int REQUEST_SUCCESS = 200;
    public static final int REQUEST_CREATED = 201;
    public static final int REQUEST_BAD_REQUEST = 400;
    public static final int REQUEST_UNAUTHORIZED = 401;
    public static final int REQUEST_NOT_FOUND = 404;
    public static final int REQUEST_SERVER_ERROR = 500;

    // User Roles (match with your backend)
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_STAFF = 2;
    public static final int ROLE_USER = 3;

    // Timeouts
    public static final int CONNECT_TIMEOUT = 30; // seconds
    public static final int READ_TIMEOUT = 30; // seconds
    public static final int WRITE_TIMEOUT = 30; // seconds

    // Validation
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 50;
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
}
