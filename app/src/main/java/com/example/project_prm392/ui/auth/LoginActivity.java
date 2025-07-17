package com.example.project_prm392.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.ui.main.MainActivity;
import com.example.project_prm392.R;
import com.example.project_prm392.data.local.SharedPrefsHelper;
import com.example.project_prm392.data.model.auth.LoginRequest;
import com.example.project_prm392.data.model.auth.LoginResponse;
import com.example.project_prm392.data.model.auth.SessionInfo;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText etUsernameOrEmail, etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;
    private TextView tvRegister, tvForgotPassword;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initServices();
        checkAutoLogin();
        setupClickListeners();
    }

    private void initViews() {
        etUsernameOrEmail = findViewById(R.id.etUsernameOrEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initServices() {
        ApiClient.initialize(this);
        apiService = ApiClient.getApiService();
        sharedPrefsHelper = new SharedPrefsHelper(this);
    }

    private void checkAutoLogin() {
        if (sharedPrefsHelper.shouldAutoLogin()) {
            navigateToMain();
            finish();
        }
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> performLogin());
        tvRegister.setOnClickListener(v -> navigateToRegister());
        tvForgotPassword.setOnClickListener(v -> navigateToForgotPassword());
    }

    private void performLogin() {
        String usernameOrEmail = etUsernameOrEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean rememberMe = cbRememberMe.isChecked();

        if (!validateInput(usernameOrEmail, password)) {
            return;
        }

        showLoading(true);

        LoginRequest request = new LoginRequest(usernameOrEmail, password, rememberMe);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    handleLoginSuccess(response.body(), rememberMe);
                } else {
                    handleLoginError(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                handleNetworkError(t);
            }
        });
    }

    private boolean validateInput(String usernameOrEmail, String password) {
        if (TextUtils.isEmpty(usernameOrEmail)) {
            etUsernameOrEmail.setError("Username or Email is required");
            etUsernameOrEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void handleLoginSuccess(LoginResponse response, boolean rememberMe) {
        try {
            String token = response.getToken();
            SessionInfo sessionInfo = response.getSessionInfo();

            Log.d(TAG, "=== Login Success ===");
            Log.d(TAG, "Token received: " + (token != null ? "YES" : "NO"));
            if (token != null) {
                Log.d(TAG, "Token length: " + token.length());
                Log.d(TAG, "Token preview: " + token.substring(0, Math.min(20, token.length())) + "...");
            }

            // Save only essential login data - user details will be loaded from profile API
            sharedPrefsHelper.saveLoginData(
                    token,
                    0, // Will be updated when profile is loaded
                    "", // Will be updated when profile is loaded
                    "", // Will be updated when profile is loaded
                    "", // Will be updated when profile is loaded
                    response.getRole(),
                    "", // Will be updated when profile is loaded
                    "", // Will be updated when profile is loaded
                    sessionInfo != null ? sessionInfo.getSessionId() : "",
                    rememberMe
            );

            // Verify token was saved
            String savedToken = sharedPrefsHelper.getToken();
            Log.d(TAG, "Token saved successfully: " + (savedToken != null && !savedToken.isEmpty()));

            if (savedToken != null) {
                Log.d(TAG, "Saved token preview: " + savedToken.substring(0, Math.min(20, savedToken.length())) + "...");
            }

            showToast("Login successful!");
            navigateToMain();
            finish();

        } catch (Exception e) {
            Log.e(TAG, "Error processing login response", e);
            showToast("Login failed. Please try again.");
        }
    }

    private void handleLoginError(int code, String message) {
        String errorMessage;
        switch (code) {
            case 400:
                errorMessage = "Invalid request. Please check your input.";
                break;
            case 401:
                errorMessage = "Invalid username/email or password.";
                break;
            case 500:
                errorMessage = "Server error. Please try again later.";
                break;
            default:
                errorMessage = "Login failed: " + message;
        }
        showToast(errorMessage);
        Log.e(TAG, "Login error: " + code + " - " + message);
    }

    private void handleNetworkError(Throwable t) {
        String errorMessage = "Network error. Please check your connection.";
        showToast(errorMessage);
        Log.e(TAG, "Network error during login", t);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        btnLogin.setText(show ? "Logging in..." : "Login");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToForgotPassword() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
