package com.example.project_prm392.ui.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.project_prm392.R;
import com.example.project_prm392.data.local.SharedPrefsHelper;
import com.example.project_prm392.data.model.auth.LogoutResponse;
import com.example.project_prm392.data.model.user.*;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.ui.auth.LoginActivity;
import com.example.project_prm392.utils.ValidationUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";

    // UI Components
    private Toolbar toolbar;
    private EditText etFullName, etPhone, etAddress;
    private TextView tvUsername, tvEmail, tvCreateDate;
    private Button btnUpdateProfile, btnChangePassword;
    private LinearLayout llLogout;
    private ProgressBar progressBar;

    // Services
    private ApiService apiService;
    private UserProfileResponse currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Debug: Check token exists
        SharedPrefsHelper prefs = new SharedPrefsHelper(this);
        String token = prefs.getToken();
        Log.d(TAG, "=== Profile Activity Token Check ===");
        Log.d(TAG, "Token exists: " + (token != null));
        if (token != null) {
            Log.d(TAG, "Token length: " + token.length());
            Log.d(TAG, "Token preview: " + token.substring(0, Math.min(20, token.length())) + "...");
        }

        initViews();
        initServices();
        setupToolbar();
        setupClickListeners();
        loadUserProfile();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvCreateDate = findViewById(R.id.tvCreateDate);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        llLogout = findViewById(R.id.llLogout);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initServices() {
        apiService = ApiClient.getApiService();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Profile");
        }
    }

    private void setupClickListeners() {
        btnUpdateProfile.setOnClickListener(v -> updateProfile());
        btnChangePassword.setOnClickListener(v -> openChangePassword());
        toolbar.setNavigationOnClickListener(v -> finish());

        // Add logout click listener
        llLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void loadUserProfile() {
        showLoading(true);
        Log.d(TAG, "=== Starting loadUserProfile ===");
        Log.d(TAG, "API Base URL: " + ApiClient.getClient().baseUrl());

        apiService.getUserProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                showLoading(false);
                Log.d(TAG, "=== API Response Received ===");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                Log.d(TAG, "Response headers: " + response.headers());

                if (response.isSuccessful() && response.body() != null) {
                    currentProfile = response.body();
                    Log.d(TAG, "✅ Profile loaded successfully");
                    Log.d(TAG, "Profile data: " + currentProfile.toString());
                    Log.d(TAG, "Username: " + currentProfile.getUsername());
                    Log.d(TAG, "Email: " + currentProfile.getEmail());
                    Log.d(TAG, "FullName: " + currentProfile.getFullName());
                    populateProfile(currentProfile);
                } else {
                    Log.e(TAG, "❌ Response failed");
                    Log.e(TAG, "Error code: " + response.code());
                    Log.e(TAG, "Error message: " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    showToast("Failed to load profile: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "❌ Network failure", t);
                Log.e(TAG, "Error message: " + t.getMessage());
                showToast("Network error: " + t.getMessage());
            }
        });
    }

    private void populateProfile(UserProfileResponse profile) {
        Log.d(TAG, "=== Populating Profile UI ===");

        tvUsername.setText(profile.getUsername() != null ? profile.getUsername() : "");
        tvEmail.setText(profile.getEmail() != null ? profile.getEmail() : "");
        etFullName.setText(profile.getFullName() != null ? profile.getFullName() : "");
        etPhone.setText(profile.getPhone() != null ? profile.getPhone() : "");
        etAddress.setText(profile.getAddress() != null ? profile.getAddress() : "");

        // Handle createDate as String and format it
        if (profile.getCreateDate() != null && !profile.getCreateDate().isEmpty()) {
            try {
                // Parse the .NET datetime format: "2025-07-17T02:19:26.503"
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                Date date = inputFormat.parse(profile.getCreateDate());
                if (date != null) {
                    tvCreateDate.setText("Member since: " + outputFormat.format(date));
                } else {
                    tvCreateDate.setText("Member since: " + profile.getCreateDate().substring(0, 10)); // Just take the date part
                }
            } catch (Exception e) {
                Log.w(TAG, "Error parsing date: " + profile.getCreateDate(), e);
                // Fallback: just show the raw date or extract the date part
                String dateOnly = profile.getCreateDate().length() >= 10 ? profile.getCreateDate().substring(0, 10) : profile.getCreateDate();
                tvCreateDate.setText("Member since: " + dateOnly);
            }
        } else {
            tvCreateDate.setText("Member since: Unknown");
        }

        Log.d(TAG, "UI populated with:");
        Log.d(TAG, "- Username: " + profile.getUsername());
        Log.d(TAG, "- Email: " + profile.getEmail());
        Log.d(TAG, "- FullName: " + profile.getFullName());
        Log.d(TAG, "- CreateDate: " + profile.getCreateDate());

        // Update SharedPrefs with actual user data
        SharedPrefsHelper prefs = new SharedPrefsHelper(this);
        String currentToken = prefs.getToken();

        prefs.saveLoginData(
                currentToken,
                profile.getUserId(),
                profile.getUsername(),
                profile.getFullName(),
                profile.getEmail(),
                prefs.getRole(), // Keep existing role
                profile.getPhone(),
                profile.getAddress(),
                prefs.getSessionId(), // Keep existing session
                prefs.isRememberMe() // Keep existing remember me
        );

        Log.d(TAG, "Updated SharedPrefs with profile data");
    }

    private void updateProfile() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (!validateInput(fullName, phone)) {
            return;
        }

        showLoading(true);

        UserProfileUpdateRequest request = new UserProfileUpdateRequest(
                fullName,
                phone.isEmpty() ? null : phone,
                address.isEmpty() ? null : address
        );

        apiService.updateUserProfile(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showLoading(false);

                if (response.isSuccessful()) {
                    showToast("Profile updated successfully!");
                    // Refresh profile data
                    loadUserProfile();
                } else {
                    handleUpdateError(response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showLoading(false);
                showToast("Network error");
                Log.e(TAG, "Network error updating profile", t);
            }
        });
    }

    private boolean validateInput(String fullName, String phone) {
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return false;
        }

        if (fullName.length() < 2) {
            etFullName.setError("Full name must be at least 2 characters");
            etFullName.requestFocus();
            return false;
        }

        if (!phone.isEmpty() && !ValidationUtils.isValidPhone(phone)) {
            etPhone.setError("Invalid phone number");
            etPhone.requestFocus();
            return false;
        }

        return true;
    }

    private void handleUpdateError(int code) {
        String errorMessage;
        switch (code) {
            case 400:
                errorMessage = "Invalid input. Please check your data.";
                break;
            case 401:
                errorMessage = "Authentication failed. Please login again.";
                break;
            case 403:
                errorMessage = "Access denied.";
                break;
            default:
                errorMessage = "Failed to update profile. Please try again.";
        }
        showToast(errorMessage);
        Log.e(TAG, "Update profile error: " + code);
    }

    private void openChangePassword() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setIcon(R.drawable.ic_logout)
                .setPositiveButton("Logout", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void performLogout() {
        showLoading(true);

        // Call logout API
        apiService.logout().enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                showLoading(false);

                // Clear local data regardless of API response
                clearUserData();

                if (response.isSuccessful()) {
                    Log.d(TAG, "Logout successful on server");
                    showToast("Logged out successfully");
                } else {
                    Log.w(TAG, "Logout API failed, but continuing with local logout");
                    showToast("Logged out locally");
                }

                navigateToLogin();
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Logout API failed", t);

                // Still clear local data even if API fails
                clearUserData();
                showToast("Logged out locally");
                navigateToLogin();
            }
        });
    }

    private void clearUserData() {
        // Clear SharedPreferences
        SharedPrefsHelper prefs = new SharedPrefsHelper(this);
        prefs.clearLoginData();

        Log.d(TAG, "User data cleared from device");
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        // Add smooth transition
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnUpdateProfile.setEnabled(!show);
        btnChangePassword.setEnabled(!show);
        btnUpdateProfile.setText(show ? "Updating..." : "Update Profile");

        // Disable logout during loading
        llLogout.setEnabled(!show);
        llLogout.setAlpha(show ? 0.5f : 1.0f);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}