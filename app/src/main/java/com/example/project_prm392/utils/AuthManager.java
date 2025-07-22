package com.example.project_prm392.utils;

import android.content.Context;
import android.util.Log;

import com.example.project_prm392.data.local.SharedPrefsHelper;
import com.example.project_prm392.data.model.auth.LogoutResponse;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthManager {
    private static final String TAG = "AuthManager";

    private Context context;
    private SharedPrefsHelper sharedPrefsHelper;
    private ApiService apiService;

    public interface LogoutCallback {
        void onSuccess();
        void onError(String error);
    }

    public AuthManager(Context context) {
        this.context = context;
        this.sharedPrefsHelper = new SharedPrefsHelper(context);
        this.apiService = ApiClient.getApiService();
    }

    public void logout(LogoutCallback callback) {
        if (!sharedPrefsHelper.isLoggedIn()) {
            callback.onSuccess();
            return;
        }

        apiService.logout().enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                // Clear local data regardless of server response
                sharedPrefsHelper.clearLoginData();
                ApiClient.resetClient(); // Reset API client to remove auth headers

                if (response.isSuccessful()) {
                    Log.i(TAG, "Logout successful on server");
                } else {
                    Log.w(TAG, "Logout failed on server but cleared local data");
                }

                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                // Clear local data even if server call fails
                sharedPrefsHelper.clearLoginData();
                ApiClient.resetClient();

                Log.w(TAG, "Logout network error but cleared local data", t);
                callback.onSuccess(); // Still consider it success since we cleared local data
            }
        });
    }

    public boolean isLoggedIn() {
        return sharedPrefsHelper.isLoggedIn();
    }

    public String getUserRole() {
        int role = sharedPrefsHelper.getRole();
        switch (role) {
            case Constants.ROLE_ADMIN:
                return "Admin";
            case Constants.ROLE_STAFF:
                return "Staff";
            case Constants.ROLE_USER:
                return "User";
            default:
                return "Unknown";
        }
    }

    public boolean isAdmin() {
        return sharedPrefsHelper.getRole() == Constants.ROLE_ADMIN;
    }

    public boolean isStaff() {
        return sharedPrefsHelper.getRole() == Constants.ROLE_STAFF;
    }

    public boolean isUser() {
        return sharedPrefsHelper.getRole() == Constants.ROLE_USER;
    }

    public int getUserId() {
        return sharedPrefsHelper.getUserId(); // đảm bảo hàm này có trong SharedPrefsHelper
    }

}
