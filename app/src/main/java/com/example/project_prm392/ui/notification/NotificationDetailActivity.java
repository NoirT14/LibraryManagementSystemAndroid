package com.example.project_prm392.ui.notification;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.R;

public class NotificationDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvMessage = findViewById(R.id.tvMessage);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvType = findViewById(R.id.tvType);

        // Nhận dữ liệu từ intent
        String title = getIntent().getStringExtra("title");
        String message = getIntent().getStringExtra("message");
        String date = getIntent().getStringExtra("date");
        String type = getIntent().getStringExtra("type");

        // Set dữ liệu lên giao diện
        tvTitle.setText(title);
        tvMessage.setText(message);
        tvDate.setText("Date: " + date);
        tvType.setText("Type: " + type);
    }
}
