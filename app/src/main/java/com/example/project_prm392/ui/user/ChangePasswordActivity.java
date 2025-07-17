package com.example.project_prm392.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.project_prm392.R;
import com.example.project_prm392.data.model.user.ChangePasswordRequest;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";

    // UI Components
    private Toolbar toolbar;
    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private ProgressBar progressBar;

    // Services
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initViews();
        initServices();
        setupToolbar();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initServices() {
        apiService = ApiClient.getApiService();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Change Password");
        }
    }

    private void setupClickListeners() {
        btnChangePassword.setOnClickListener(v -> changePassword());
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!validateInput(currentPassword, newPassword, confirmPassword)) {
            return;
        }

        showLoading(true);

        ChangePasswordRequest request = new ChangePasswordRequest(currentPassword, newPassword, confirmPassword);

        apiService.changePassword(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showLoading(false);

                if (response.isSuccessful()) {
                    showToast("Password changed successfully!");
                    clearFields();
                    finish();
                } else {
                    handleChangePasswordError(response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showLoading(false);
                showToast("Network error");
                Log.e(TAG, "Network error changing password", t);
            }
        });
    }

    private boolean validateInput(String currentPassword, String newPassword, String confirmPassword) {
        // Current password validation
        if (TextUtils.isEmpty(currentPassword)) {
            etCurrentPassword.setError("Current password is required");
            etCurrentPassword.requestFocus();
            return false;
        }

        // New password validation
        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError("New password is required");
            etNewPassword.requestFocus();
            return false;
        }

        if (newPassword.length() < Constants.MIN_PASSWORD_LENGTH) {
            etNewPassword.setError("Password must be at least " + Constants.MIN_PASSWORD_LENGTH + " characters");
            etNewPassword.requestFocus();
            return false;
        }

        if (newPassword.length() > Constants.MAX_PASSWORD_LENGTH) {
            etNewPassword.setError("Password cannot exceed " + Constants.MAX_PASSWORD_LENGTH + " characters");
            etNewPassword.requestFocus();
            return false;
        }

        // Confirm password validation
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Please confirm your new password");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        // Check if new password is same as current
        if (currentPassword.equals(newPassword)) {
            etNewPassword.setError("New password must be different from current password");
            etNewPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void handleChangePasswordError(int code) {
        String errorMessage;
        switch (code) {
            case 400:
                errorMessage = "Invalid request. Please check your input.";
                break;
            case 401:
                errorMessage = "Current password is incorrect.";
                etCurrentPassword.setError("Incorrect password");
                etCurrentPassword.requestFocus();
                break;
            case 403:
                errorMessage = "Access denied.";
                break;
            default:
                errorMessage = "Failed to change password. Please try again.";
        }
        showToast(errorMessage);
        Log.e(TAG, "Change password error: " + code);
    }

    private void clearFields() {
        etCurrentPassword.setText("");
        etNewPassword.setText("");
        etConfirmPassword.setText("");
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnChangePassword.setEnabled(!show);
        btnChangePassword.setText(show ? "Changing..." : "Change Password");

        // Disable input fields during loading
        etCurrentPassword.setEnabled(!show);
        etNewPassword.setEnabled(!show);
        etConfirmPassword.setEnabled(!show);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}