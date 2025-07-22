package com.example.project_prm392.ui.notification;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.data.local.SharedPrefsHelper;
import com.example.project_prm392.data.model.notification.Notification;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.ui.notification.adapter.NotificationAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications); // 👈 phải gọi trước khi findViewById

        recyclerView = findViewById(R.id.recyclerViewNotifications); // 👈 đảm bảo ID đúng

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPrefsHelper prefs = new SharedPrefsHelper(this);
        Log.d("DEBUG", "id: " + prefs.getUserId());
        ApiService apiService = ApiClient.getApiService();
        apiService.getNotificationsByReceiver(prefs.getUserId())
                .enqueue(new Callback<List<Notification>>() {
                    @Override
                    public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                        if (response.isSuccessful()) {
                            List<Notification> data = response.body();
                            adapter = new NotificationAdapter(NotificationsActivity.this, data); // ✔️ đúng cú pháp

                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.e("NotificationActivity", "API call failed: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Notification>> call, Throwable t) {
                        Log.e("NotificationActivity", "Network error", t);
                    }
                });
    }
}
