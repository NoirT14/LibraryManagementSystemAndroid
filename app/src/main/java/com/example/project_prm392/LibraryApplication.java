package com.example.project_prm392;

import android.app.Application;
import com.example.project_prm392.data.remote.ApiClient;

public class LibraryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize API Client
        ApiClient.initialize(this);
    }
}
