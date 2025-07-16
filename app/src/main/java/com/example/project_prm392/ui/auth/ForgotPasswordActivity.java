package com.example.project_prm392.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.R;
import com.example.project_prm392.data.model.auth.ForgotPasswordRequest;
import com.example.project_prm392.data.model.auth.MessageResponse;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";

    private EditText etUsernameOrEmail;
    private Button btnSendReset;
    private TextView tvBackToLogin;
    private ProgressBar progressBar;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
        initServices();
        setupClickListeners();
    }

    private void initViews() {
        etUsernameOrEmail = findViewById(R.id.etUsernameOrEmail);
        btnSendReset = findViewById(R.id.btnSendReset);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initServices() {
        ApiClient.initialize(this);
        apiService = ApiClient.getApiService();
    }

    private void setupClickListeners() {
        btnSendReset.setOnClickListener(v -> sendResetRequest());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void sendResetRequest() {
        String usernameOrEmail = etUsernameOrEmail.getText().toString().trim();

        if (!validateInput(usernameOrEmail)) {
            return;
        }

        showLoading(true);

        ForgotPasswordRequest request = new ForgotPasswordRequest(usernameOrEmail);

        apiService.forgotPassword(request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                showLoading(false);

                if (response.isSuccessful()) {
                    showToast("If the email exists, a reset password link has been sent.");
                    finish();
                } else {
                    handleError(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                showLoading(false);
                handleNetworkError(t);
            }
        });
    }

    private boolean validateInput(String usernameOrEmail) {
        if (TextUtils.isEmpty(usernameOrEmail)) {
            etUsernameOrEmail.setError("Username or Email is required");
            etUsernameOrEmail.requestFocus();
            return false;
        }
        return true;
    }

    private void handleError(int code, String message) {
        String errorMessage;
        switch (code) {
            case 400:
                errorMessage = "Invalid request. Please check your input.";
                break;
            case 500:
                errorMessage = "Server error. Please try again later.";
                break;
            default:
                errorMessage = "Request failed: " + message;
        }
        showToast(errorMessage);
        Log.e(TAG, "Forgot password error: " + code + " - " + message);
    }

    private void handleNetworkError(Throwable t) {
        String errorMessage = "Network error. Please check your connection.";
        showToast(errorMessage);
        Log.e(TAG, "Network error during forgot password", t);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSendReset.setEnabled(!show);
        btnSendReset.setText(show ? "Sending..." : "Send Reset Link");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
