package com.example.project_prm392.data.remote;

import android.content.Context;

import com.example.project_prm392.data.local.SharedPrefsHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private SharedPrefsHelper sharedPrefsHelper;

    public AuthInterceptor(Context context) {
        this.sharedPrefsHelper = new SharedPrefsHelper(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        String token = sharedPrefsHelper.getToken();

        if (token != null && !token.isEmpty()) {
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        }

        return chain.proceed(original);
    }
}
