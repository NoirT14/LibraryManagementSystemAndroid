package com.example.project_prm392.data.remote;

import android.content.Context;
import android.util.Log;

import com.example.project_prm392.data.local.SharedPrefsHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private static final String TAG = "AuthInterceptor";
    private SharedPrefsHelper sharedPrefsHelper;

    public AuthInterceptor(Context context) {
        this.sharedPrefsHelper = new SharedPrefsHelper(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Log.d(TAG, "=== Auth Interceptor ===");
        Log.d(TAG, "Request URL: " + original.url());
        Log.d(TAG, "Request Method: " + original.method());

        String token = sharedPrefsHelper.getToken();
        Log.d(TAG, "Retrieved token: " + (token != null ? "***" + token.substring(Math.max(0, token.length() - 10)) : "NULL"));

        if (token != null && !token.isEmpty()) {
            Log.d(TAG, "✅ Adding Authorization header");
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body());

            Request request = requestBuilder.build();

            // Log headers
            Log.d(TAG, "Request headers: " + request.headers());

            Response response = chain.proceed(request);
            Log.d(TAG, "Response code: " + response.code());
            return response;
        } else {
            Log.w(TAG, "❌ No token found - proceeding without auth");
            Response response = chain.proceed(original);
            Log.d(TAG, "Response code (no auth): " + response.code());
            return response;
        }
    }
}