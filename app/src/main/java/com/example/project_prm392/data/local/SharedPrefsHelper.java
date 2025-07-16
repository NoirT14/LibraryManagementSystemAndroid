package com.example.project_prm392.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {
    private static final String PREF_NAME = "LibraryApp";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_SESSION_ID = "session_id";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_REMEMBER_ME = "remember_me";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save login data
    public void saveLoginData(String token, int userId, String username, String fullName,
                              String email, int role, String phone, String address,
                              String sessionId, boolean rememberMe) {
        editor.putString(KEY_TOKEN, token);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_EMAIL, email);
        editor.putInt(KEY_ROLE, role);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    // Get methods
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getFullName() {
        return sharedPreferences.getString(KEY_FULL_NAME, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public int getRole() {
        return sharedPreferences.getInt(KEY_ROLE, -1);
    }

    public String getPhone() {
        return sharedPreferences.getString(KEY_PHONE, null);
    }

    public String getAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, null);
    }

    public String getSessionId() {
        return sharedPreferences.getString(KEY_SESSION_ID, null);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isRememberMe() {
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }

    // Clear all data (logout)
    public void clearLoginData() {
        editor.clear();
        editor.apply();
    }

    // Update token
    public void updateToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    // Check if token exists and user should stay logged in
    public boolean shouldAutoLogin() {
        return isLoggedIn() && getToken() != null && !getToken().isEmpty();
    }
}
