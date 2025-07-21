package com.example.project_prm392.ui.auth;

import android.content.SharedPreferences;
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
import com.example.project_prm392.data.model.auth.MessageResponse;
import com.example.project_prm392.data.model.auth.ResetPasswordWithOtpRequest;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ResetPasswordActivity";

    private TextView tvEmail;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnResetPassword;
    private TextView tvBackToLogin;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SharedPreferences sharedPreferences;

    private String email;
    private String otpCode;
    private boolean isFromOtp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initViews();
        initServices();
        setupClickListeners();
        loadDataFromIntent();
    }

    private void initViews() {
        tvEmail = findViewById(R.id.tvEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initServices() {
        ApiClient.initialize(this);
        apiService = ApiClient.getApiService();
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
    }

    private void setupClickListeners() {
        btnResetPassword.setOnClickListener(v -> resetPassword());
        tvBackToLogin.setOnClickListener(v -> {
            // ✅ Clear OTP verification status when going back
            clearOtpVerification();
            finish();
        });
    }

    private void loadDataFromIntent() {
        email = getIntent().getStringExtra(Constants.EXTRA_EMAIL);
        otpCode = getIntent().getStringExtra("otpCode");
        isFromOtp = getIntent().getBooleanExtra(Constants.EXTRA_FROM_OTP, false);

        // ✅ If email not in intent, try SharedPreferences
        if (TextUtils.isEmpty(email)) {
            email = sharedPreferences.getString(Constants.KEY_RESET_EMAIL, "");
        }

        // ✅ Display email to user
        if (!TextUtils.isEmpty(email)) {
            tvEmail.setText("Reset password for: " + email);
        }

        // ✅ Validate that we have required data for OTP-based reset
        if (!isFromOtp || TextUtils.isEmpty(email) || TextUtils.isEmpty(otpCode)) {
            showToast("Invalid reset session. Please start over.");
            finish();
            return;
        }
    }

    private void resetPassword() {
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!validateInput(newPassword, confirmPassword)) {
            return;
        }

        showLoading(true);

        // ✅ Use OTP-based reset password endpoint
        ResetPasswordWithOtpRequest request = new ResetPasswordWithOtpRequest(
                email, otpCode, newPassword, confirmPassword);

        apiService.resetPasswordWithOtp(request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    MessageResponse messageResponse = response.body();
                    showToast(messageResponse.getMessage());

                    // ✅ Clear OTP verification status
                    clearOtpVerification();

                    // ✅ Set result and finish
                    setResult(RESULT_OK);
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

    private boolean validateInput(String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError(Constants.VALIDATION_REQUIRED);
            etNewPassword.requestFocus();
            return false;
        }

        if (newPassword.length() < Constants.MIN_PASSWORD_LENGTH) {
            etNewPassword.setError(Constants.VALIDATION_PASSWORD_LENGTH);
            etNewPassword.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError(Constants.VALIDATION_REQUIRED);
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError(Constants.VALIDATION_PASSWORDS_MATCH);
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void clearOtpVerification() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.KEY_RESET_EMAIL);
        editor.remove(Constants.KEY_OTP_VERIFIED);
        editor.apply();
    }

    private void handleError(int code, String message) {
        String errorMessage;
        switch (code) {
            case Constants.HTTP_BAD_REQUEST:
                errorMessage = "Invalid request. Please check your input.";
                break;
            case Constants.HTTP_UNAUTHORIZED:
                errorMessage = Constants.ERROR_OTP_EXPIRED;
                break;
            case Constants.HTTP_INTERNAL_SERVER_ERROR:
                errorMessage = Constants.ERROR_SERVER;
                break;
            default:
                errorMessage = "Reset failed: " + message;
        }
        showToast(errorMessage);
        Log.e(TAG, "Password reset error: " + code + " - " + message);
    }

    private void handleNetworkError(Throwable t) {
        showToast(Constants.ERROR_NETWORK);
        Log.e(TAG, "Network error during password reset", t);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnResetPassword.setEnabled(!show);
        btnResetPassword.setText(show ? Constants.LOADING_RESET_PASSWORD : "Reset Password");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        clearOtpVerification();
        super.onBackPressed();
    }
}