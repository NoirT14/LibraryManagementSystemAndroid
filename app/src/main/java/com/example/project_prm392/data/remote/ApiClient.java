package com.example.project_prm392.data.remote;

import android.content.Context;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:5027/";

    private static Retrofit retrofit;
    private static Context context;

    public static void initialize(Context ctx) {
        context = ctx.getApplicationContext();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);

            if (context != null) {
                AuthInterceptor authInterceptor = new AuthInterceptor(context);
                clientBuilder.addInterceptor(authInterceptor);
            }

            OkHttpClient client = clientBuilder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }

    public static void resetClient() {
        retrofit = null;
    }
}