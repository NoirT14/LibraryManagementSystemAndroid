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

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

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


    // Lấy danh sách sách (homepage)
    @GET("odata/Books")
    Call<List<Book>> getBooks();

    // Lấy chi tiết sách theo ID
    @GET("odata/Books/{id}")
    Call<Book> getBookById(@Path("id") int id);
}
