package com.example.project_prm392.data.remote;

import com.example.project_prm392.data.model.BookBasicInfoRespone;
import com.example.project_prm392.data.model.BookDetailInfoResponse;
import com.example.project_prm392.data.model.ODataResponse;
import com.example.project_prm392.data.model.auth.AuthResult;
import com.example.project_prm392.data.model.auth.ForgotPasswordRequest;
import com.example.project_prm392.data.model.auth.LoginRequest;
import com.example.project_prm392.data.model.auth.LoginResponse;
import com.example.project_prm392.data.model.auth.LogoutResponse;
import com.example.project_prm392.data.model.auth.MessageResponse;
import com.example.project_prm392.data.model.auth.RegisterRequest;
import com.example.project_prm392.data.model.auth.ResetPasswordRequest;
import com.example.project_prm392.data.model.user.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
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

    @POST("api/auth/forgot-password")
    Call<MessageResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("api/auth/reset-password")
    Call<MessageResponse> resetPassword(@Body ResetPasswordRequest request);

    // ============= USER PROFILE ENDPOINTS =============
    @GET("api/user/profile")
    Call<UserProfileResponse> getUserProfile();

    @PUT("api/user/profile")
    Call<Void> updateUserProfile(@Body UserProfileUpdateRequest request);

    @PUT("api/user/change-password")
    Call<Void> changePassword(@Body ChangePasswordRequest request);


    // ============= Book ENDPOINTS =============
    // Lấy danh sách sách (homepage)
    @GET("api/manage/Book")
    Call<ODataResponse<BookBasicInfoRespone>> getBooks(
            @Query("$skip") int skip,
            @Query("$top") int top,
            @Query("$filter") String filter
    );

    // Lấy chi tiết sách theo ID
    @GET("api/manage/Book/{id}")
    Call<BookDetailInfoResponse> getBookById(@Path("id") int id);
}
