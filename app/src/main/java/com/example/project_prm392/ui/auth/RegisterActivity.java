package com.example.project_prm392.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.R;
import com.example.project_prm392.data.model.auth.AuthResult;
import com.example.project_prm392.data.model.auth.RegisterRequest;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private EditText etUsername, etEmail, etPassword, etConfirmPassword, etFullName, etPhone, etAddress;
    private Button btnRegister;
    private TextView tvLogin;
    private ProgressBar progressBar;

    private ApiService apiService;
    private Patterns Patterns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initServices();
        setupClickListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initServices() {
        ApiClient.initialize(this);
        apiService = ApiClient.getApiService();
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> performRegister());
        tvLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void performRegister() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (!validateInput(username, email, password, confirmPassword, fullName)) {
            return;
        }

        showLoading(true);

        RegisterRequest request = new RegisterRequest(username, email, password, fullName);
        if (!TextUtils.isEmpty(phone)) {
            request.setPhone(phone);
        }
        if (!TextUtils.isEmpty(address)) {
            request.setAddress(address);
        }

        apiService.register(request).enqueue(new Callback<AuthResult>() {
            @Override
            public void onResponse(Call<AuthResult> call, Response<AuthResult> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    handleRegisterResponse(response.body());
                } else {
                    handleRegisterError(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResult> call, Throwable t) {
                showLoading(false);
                handleNetworkError(t);
            }
        });
    }

    private boolean validateInput(String username, String email, String password,
                                  String confirmPassword, String fullName) {

        // Username validation
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return false;
        }
        if (username.length() <  Constants.MIN_USERNAME_LENGTH) {
            etUsername.setError("Username must be at least " + Constants.MIN_USERNAME_LENGTH + " characters");
            etUsername.requestFocus();
            return false;
        }
        if (username.length() > Constants.MAX_USERNAME_LENGTH) {
            etUsername.setError("Username cannot exceed " + Constants.MAX_USERNAME_LENGTH + " characters");
            etUsername.requestFocus();
            return false;
        }

        // Email validation
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return false;
        }

        // Password validation
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }
        if (password.length() < Constants.MIN_PASSWORD_LENGTH) {
            etPassword.setError("Password must be at least " + Constants.MIN_PASSWORD_LENGTH + " characters");
            etPassword.requestFocus();
            return false;
        }
        if (password.length() > Constants.MAX_PASSWORD_LENGTH) {
            etPassword.setError("Password cannot exceed " + Constants.MAX_PASSWORD_LENGTH + " characters");
            etPassword.requestFocus();
            return false;
        }

        // Confirm password validation
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Please confirm your password");
            etConfirmPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        // Full name validation
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

        return true;
    }

    private void handleRegisterResponse(AuthResult result) {
        if (result.isSuccess()) {
            showToast("Registration successful! Please login with your credentials.");
            navigateToLogin();
            finish();
        } else {
            String errorMessage = result.getErrorMessage();
            if (errorMessage != null && !errorMessage.isEmpty()) {
                showToast(errorMessage);
            } else {
                showToast("Registration failed. Please try again.");
            }
        }
    }

    private void handleRegisterError(int code, String message) {
        String errorMessage;
        switch (code) {
            case 400:
                errorMessage = "Invalid input. Please check your information.";
                break;
            case 409:
                errorMessage = "Username or email already exists.";
                break;
            case 500:
                errorMessage = "Server error. Please try again later.";
                break;
            default:
                errorMessage = "Registration failed: " + message;
        }
        showToast(errorMessage);
        Log.e(TAG, "Registration error: " + code + " - " + message);
    }

    private void handleNetworkError(Throwable t) {
        String errorMessage = "Network error. Please check your connection.";
        showToast(errorMessage);
        Log.e(TAG, "Network error during registration", t);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
        btnRegister.setText(show ? "Creating Account..." : "Register");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
