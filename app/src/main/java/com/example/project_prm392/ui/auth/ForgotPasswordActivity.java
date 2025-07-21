package com.example.project_prm392.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.project_prm392.data.model.auth.ForgotPasswordRequest;
import com.example.project_prm392.data.model.auth.MessageResponse;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";

    private EditText etUsernameOrEmail;
    private Button btnSendOtp;
    private TextView tvBackToLogin;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SharedPreferences sharedPreferences;

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
        btnSendOtp = findViewById(R.id.btnSendOtp); // ✅ Updated button ID
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initServices() {
        ApiClient.initialize(this);
        apiService = ApiClient.getApiService();
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
    }

    private void setupClickListeners() {
        btnSendOtp.setOnClickListener(v -> sendOtpRequest());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void sendOtpRequest() {
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

                if (response.isSuccessful() && response.body() != null) {
                    MessageResponse messageResponse = response.body();

                    // ✅ Save email for OTP verification if it's a valid email
                    String emailToSave = extractEmailFromInput(usernameOrEmail);
                    if (emailToSave != null) {
                        saveEmailForOtpVerification(emailToSave);
                    }

                    showToast(messageResponse.getMessage());

                    // ✅ Navigate to OTP verification screen
                    navigateToOtpVerification(emailToSave);

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
            etUsernameOrEmail.setError(Constants.VALIDATION_REQUIRED);
            etUsernameOrEmail.requestFocus();
            return false;
        }

        // ✅ Additional validation for email format if it looks like an email
        if (usernameOrEmail.contains("@") && !Patterns.EMAIL_ADDRESS.matcher(usernameOrEmail).matches()) {
            etUsernameOrEmail.setError(Constants.VALIDATION_EMAIL);
            etUsernameOrEmail.requestFocus();
            return false;
        }

        return true;
    }

    // ✅ NEW: Extract email from input (if it's an email)
    private String extractEmailFromInput(String input) {
        if (input.contains("@") && Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            return input;
        }
        return null; // It's a username, we'll need the email from the OTP screen
    }

    // ✅ NEW: Save email for OTP verification
    private void saveEmailForOtpVerification(String email) {
        if (email != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.KEY_RESET_EMAIL, email);
            editor.apply();
        }
    }

    // ✅ NEW: Navigate to OTP verification
    private void navigateToOtpVerification(String email) {
        Intent intent = new Intent(this, VerifyOtpActivity.class);
        if (email != null) {
            intent.putExtra(Constants.EXTRA_EMAIL, email);
        }
        startActivityForResult(intent, Constants.REQUEST_VERIFY_OTP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_VERIFY_OTP && resultCode == RESULT_OK) {
            // ✅ OTP verified successfully, close this activity
            showToast(Constants.SUCCESS_OTP_VERIFIED);
            finish();
        }
    }

    private void handleError(int code, String message) {
        String errorMessage;
        switch (code) {
            case Constants.HTTP_BAD_REQUEST:
                errorMessage = Constants.ERROR_VALIDATION;
                break;
            case Constants.HTTP_INTERNAL_SERVER_ERROR:
                errorMessage = Constants.ERROR_SERVER;
                break;
            default:
                errorMessage = "Request failed: " + message;
        }
        showToast(errorMessage);
        Log.e(TAG, "Forgot password error: " + code + " - " + message);
    }

    private void handleNetworkError(Throwable t) {
        showToast(Constants.ERROR_NETWORK);
        Log.e(TAG, "Network error during forgot password", t);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSendOtp.setEnabled(!show);
        btnSendOtp.setText(show ? Constants.LOADING_SEND_RESET : "Send OTP"); // ✅ Updated text
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}