package com.example.project_prm392.data.remote;

import com.example.project_prm392.data.model.Book;
import com.example.project_prm392.data.model.auth.AuthResult;
import com.example.project_prm392.data.model.auth.ForgotPasswordRequest;
import com.example.project_prm392.data.model.auth.LoginRequest;
import com.example.project_prm392.data.model.auth.LoginResponse;
import com.example.project_prm392.data.model.auth.LogoutResponse;
import com.example.project_prm392.data.model.auth.MessageResponse;
import com.example.project_prm392.data.model.auth.RegisterRequest;
import com.example.project_prm392.data.model.auth.ResetPasswordRequest;
import com.example.project_prm392.data.model.auth.ResetPasswordWithOtpRequest;
import com.example.project_prm392.data.model.auth.VerifyOtpRequest;
import com.example.project_prm392.data.model.auth.VerifyOtpResponse;
import com.example.project_prm392.data.model.user.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ============= AUTH ENDPOINTS =============
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/auth/register")
    Call<AuthResult> register(@Body RegisterRequest request);

    @POST("api/auth/logout")
    Call<LogoutResponse> logout();

    // ✅ UPDATED: Now sends OTP instead of reset link
    @POST("api/auth/forgot-password")
    Call<MessageResponse> forgotPassword(@Body ForgotPasswordRequest request);

    // ✅ NEW: OTP verification endpoint
    @POST("api/auth/verify-otp")
    Call<VerifyOtpResponse> verifyOtp(@Body VerifyOtpRequest request);

    // ✅ NEW: Reset password with OTP endpoint
    @POST("api/auth/reset-password-with-otp")
    Call<MessageResponse> resetPasswordWithOtp(@Body ResetPasswordWithOtpRequest request);

    // ✅ DEPRECATED: Keep for backward compatibility
    @POST("api/auth/reset-password")
    Call<MessageResponse> resetPassword(@Body ResetPasswordRequest request);

    // ============= USER PROFILE ENDPOINTS =============
    @GET("api/user/profile")
    Call<UserProfileResponse> getUserProfile();

    @PUT("api/user/profile")
    Call<Void> updateUserProfile(@Body UserProfileUpdateRequest request);

    @PUT("api/user/change-password")
    Call<Void> changePassword(@Body ChangePasswordRequest request);

    // ============= BOOK ENDPOINTS =============
    // Lấy danh sách sách (homepage)
    @GET("odata/Books")
    Call<List<Book>> getBooks();

    // Lấy chi tiết sách theo ID
    @GET("odata/Books/{id}")
    Call<Book> getBookById(@Path("id") int id);
}