package com.example.project_prm392.utils;

public class Constants {

    // API Configuration
    public static final String BASE_URL = "http://10.0.2.2:5027/"; // Your backend URL

    // API Endpoints
    public static final String LOGIN_ENDPOINT = "api/auth/login";
    public static final String REGISTER_ENDPOINT = "api/auth/register";
    public static final String LOGOUT_ENDPOINT = "api/auth/logout";
    public static final String FORGOT_PASSWORD_ENDPOINT = "api/auth/forgot-password";
    public static final String RESET_PASSWORD_ENDPOINT = "api/auth/reset-password";
    public static final String USER_PROFILE_ENDPOINT = "api/user/profile";
    public static final String CHANGE_PASSWORD_ENDPOINT = "api/user/change-password";

    // HTTP Status Codes
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_NO_CONTENT = 204;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_CONFLICT = 409;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;

    // User Roles (match with your backend)
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_STAFF = 2;
    public static final int ROLE_USER = 3;

    // Validation Constants
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 50;
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MIN_FULL_NAME_LENGTH = 2;
    public static final int MAX_FULL_NAME_LENGTH = 100;
    public static final int MIN_PHONE_LENGTH = 10;
    public static final int MAX_PHONE_LENGTH = 15;
    public static final int MAX_ADDRESS_LENGTH = 255;

    // Network Timeouts (seconds)
    public static final int CONNECT_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 30;

    // SharedPreferences Keys
    public static final String PREF_NAME = "LibraryApp";
    public static final String KEY_TOKEN = "auth_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULL_NAME = "full_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ROLE = "role";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_SESSION_ID = "session_id";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_REMEMBER_ME = "remember_me";

    // Intent Extra Keys
    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_USER_DATA = "user_data";
    public static final String EXTRA_BOOK_ID = "book_id";
    public static final String EXTRA_BOOK_DATA = "book_data";

    // Request Codes
    public static final int REQUEST_CREATE_USER = 100;
    public static final int REQUEST_EDIT_USER = 101;
    public static final int REQUEST_EDIT_PROFILE = 102;
    public static final int REQUEST_CHANGE_PASSWORD = 103;

    // Date Formats
    public static final String DATE_FORMAT_DISPLAY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_SHORT = "dd/MM/yy";

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    // File Upload
    public static final int MAX_FILE_SIZE_MB = 10;
    public static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/jpg"};

    // App Configuration
    public static final String APP_NAME = "Library Management";
    public static final String APP_VERSION = "1.0.0";
    public static final boolean DEBUG_MODE = true;

    // Error Messages
    public static final String ERROR_NETWORK = "Network error. Please check your connection.";
    public static final String ERROR_SERVER = "Server error. Please try again later.";
    public static final String ERROR_UNAUTHORIZED = "Authentication failed. Please login again.";
    public static final String ERROR_FORBIDDEN = "Access denied. You don't have permission.";
    public static final String ERROR_NOT_FOUND = "Resource not found.";
    public static final String ERROR_CONFLICT = "Data already exists.";
    public static final String ERROR_VALIDATION = "Please check your input.";
    public static final String ERROR_UNKNOWN = "An unexpected error occurred.";

    // Success Messages
    public static final String SUCCESS_LOGIN = "Login successful!";
    public static final String SUCCESS_REGISTER = "Registration successful!";
    public static final String SUCCESS_LOGOUT = "Logged out successfully";
    public static final String SUCCESS_PROFILE_UPDATE = "Profile updated successfully!";
    public static final String SUCCESS_PASSWORD_CHANGE = "Password changed successfully!";
    public static final String SUCCESS_PASSWORD_RESET = "Password reset link sent!";

    // Loading Messages
    public static final String LOADING_LOGIN = "Logging in...";
    public static final String LOADING_REGISTER = "Creating account...";
    public static final String LOADING_UPDATE = "Updating...";
    public static final String LOADING_CHANGE_PASSWORD = "Changing password...";
    public static final String LOADING_SEND_RESET = "Sending...";

    // Validation Messages
    public static final String VALIDATION_REQUIRED = "This field is required";
    public static final String VALIDATION_EMAIL = "Please enter a valid email address";
    public static final String VALIDATION_PASSWORD_LENGTH = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
    public static final String VALIDATION_USERNAME_LENGTH = "Username must be " + MIN_USERNAME_LENGTH + "-" + MAX_USERNAME_LENGTH + " characters";
    public static final String VALIDATION_PASSWORDS_MATCH = "Passwords do not match";
    public static final String VALIDATION_PHONE = "Please enter a valid phone number";
    public static final String VALIDATION_FULL_NAME = "Full name must be at least " + MIN_FULL_NAME_LENGTH + " characters";

    // Allowed Email Domains (if needed)
    public static final String[] ALLOWED_EMAIL_DOMAINS = {
            "gmail.com", "yahoo.com", "hotmail.com", "outlook.com",
            "fpt.edu.vn", "student.fpt.edu.vn"
    };

    // Vietnam Phone Prefixes
    public static final String[] VIETNAM_MOBILE_PREFIXES = {
            "03", "05", "07", "08", "09"
    };

    // Default Values
    public static final String DEFAULT_AVATAR = "default_avatar.png";
    public static final String DEFAULT_COVER_IMAGE = "default_cover.jpg";

    // Cache Configuration
    public static final long CACHE_DURATION_MINUTES = 30;
    public static final int MAX_CACHE_SIZE_MB = 50;

    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
}