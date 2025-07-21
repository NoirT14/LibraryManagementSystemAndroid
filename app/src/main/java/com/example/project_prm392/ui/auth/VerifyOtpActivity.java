package com.example.project_prm392.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.example.project_prm392.data.model.auth.VerifyOtpRequest;
import com.example.project_prm392.data.model.auth.VerifyOtpResponse;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpActivity extends AppCompatActivity {
    private static final String TAG = "VerifyOtpActivity";

    private EditText etEmail;
    private EditText etOtpCode;
    private Button btnVerifyOtp;
    private Button btnResendOtp;
    private TextView tvBackToForgot;
    private TextView tvCountdown;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SharedPreferences sharedPreferences;
    private CountDownTimer countDownTimer;

    private String currentEmail;
    private boolean isResendEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        initViews();
        initServices();
        setupClickListeners();
        loadEmailFromIntent();
        startCountdown();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etOtpCode = findViewById(R.id.etOtpCode);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        btnResendOtp = findViewById(R.id.btnResendOtp);
        tvBackToForgot = findViewById(R.id.tvBackToForgot);
        tvCountdown = findViewById(R.id.tvCountdown);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initServices() {
        ApiClient.initialize(this);
        apiService = ApiClient.getApiService();
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
    }

    private void setupClickListeners() {
        btnVerifyOtp.setOnClickListener(v -> verifyOtp());
        btnResendOtp.setOnClickListener(v -> resendOtp());
        tvBackToForgot.setOnClickListener(v -> finish());
    }

    private void loadEmailFromIntent() {
        // Try to get email from intent first
        currentEmail = getIntent().getStringExtra(Constants.EXTRA_EMAIL);

        // If not found, try to get from SharedPreferences
        if (currentEmail == null) {
            currentEmail = sharedPreferences.getString(Constants.KEY_RESET_EMAIL, "");
        }

        // Pre-fill email if available
        if (!TextUtils.isEmpty(currentEmail)) {
            etEmail.setText(currentEmail);
            etEmail.setEnabled(false); // Disable editing if we have the email
        }
    }

    private void startCountdown() {
        isResendEnabled = false;
        btnResendOtp.setEnabled(false);

        // 10 minutes countdown (same as OTP expiry)
        countDownTimer = new CountDownTimer(Constants.OTP_EXPIRY_MINUTES * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;

                tvCountdown.setText(String.format("Resend OTP in %02d:%02d", minutes, seconds));

                // Enable resend button after 1 minute
                if (millisUntilFinished <= (Constants.OTP_EXPIRY_MINUTES - 1) * 60 * 1000 && !isResendEnabled) {
                    isResendEnabled = true;
                    btnResendOtp.setEnabled(true);
                    btnResendOtp.setText("Resend OTP");
                }
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("OTP expired. Please request a new one.");
                isResendEnabled = true;
                btnResendOtp.setEnabled(true);
                btnResendOtp.setText("Resend OTP");
            }
        }.start();
    }

    private void verifyOtp() {
        String email = etEmail.getText().toString().trim();
        String otpCode = etOtpCode.getText().toString().trim();

        if (!validateInput(email, otpCode)) {
            return;
        }

        showLoading(true);

        VerifyOtpRequest request = new VerifyOtpRequest(email, otpCode);

        apiService.verifyOtp(request).enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    VerifyOtpResponse otpResponse = response.body();

                    if (otpResponse.isSuccess()) {
                        // ✅ Save verified email and navigate to reset password
                        saveVerifiedEmail(email);
                        showToast(otpResponse.getMessage());
                        navigateToResetPassword(email, otpCode);
                    } else {
                        showToast(otpResponse.getMessage());
                    }
                } else {
                    handleError(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                showLoading(false);
                handleNetworkError(t);
            }
        });
    }

    private void resendOtp() {
        String email = etEmail.getText().toString().trim();

        if (!validateEmail(email)) {
            return;
        }

        showLoading(true);

        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        apiService.forgotPassword(request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    showToast("New OTP has been sent to your email!");

                    // ✅ Restart countdown
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    startCountdown();

                    // Clear OTP field
                    etOtpCode.setText("");

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

    private boolean validateInput(String email, String otpCode) {
        if (!validateEmail(email)) {
            return false;
        }

        if (TextUtils.isEmpty(otpCode)) {
            etOtpCode.setError(Constants.VALIDATION_REQUIRED);
            etOtpCode.requestFocus();
            return false;
        }

        if (otpCode.length() != Constants.OTP_LENGTH) {
            etOtpCode.setError(Constants.VALIDATION_OTP_LENGTH);
            etOtpCode.requestFocus();
            return false;
        }

        if (!otpCode.matches("\\d+")) {
            etOtpCode.setError(Constants.VALIDATION_OTP_DIGITS);
            etOtpCode.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(Constants.VALIDATION_REQUIRED);
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(Constants.VALIDATION_EMAIL);
            etEmail.requestFocus();
            return false;
        }

        return true;
    }

    private void saveVerifiedEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_RESET_EMAIL, email);
        editor.putBoolean(Constants.KEY_OTP_VERIFIED, true);
        editor.apply();
    }

    private void navigateToResetPassword(String email, String otpCode) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra(Constants.EXTRA_EMAIL, email);
        intent.putExtra("otpCode", otpCode); // Pass OTP code for final verification
        intent.putExtra(Constants.EXTRA_FROM_OTP, true);
        startActivityForResult(intent, Constants.REQUEST_RESET_PASSWORD_OTP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_RESET_PASSWORD_OTP && resultCode == RESULT_OK) {
            // ✅ Password reset successfully, finish this activity
            setResult(RESULT_OK);
            finish();
        }
    }

    private void handleError(int code, String message) {
        String errorMessage;
        switch (code) {
            case Constants.HTTP_BAD_REQUEST:
                errorMessage = Constants.ERROR_OTP_INVALID;
                break;
            case Constants.HTTP_INTERNAL_SERVER_ERROR:
                errorMessage = Constants.ERROR_SERVER;
                break;
            default:
                errorMessage = "Verification failed: " + message;
        }
        showToast(errorMessage);
        Log.e(TAG, "OTP verification error: " + code + " - " + message);
    }

    private void handleNetworkError(Throwable t) {
        showToast(Constants.ERROR_NETWORK);
        Log.e(TAG, "Network error during OTP verification", t);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnVerifyOtp.setEnabled(!show);
        btnResendOtp.setEnabled(!show && isResendEnabled);
        btnVerifyOtp.setText(show ? Constants.LOADING_VERIFY_OTP : "Verify OTP");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}