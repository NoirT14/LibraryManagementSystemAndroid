package com.example.project_prm392.ui.notification.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.data.model.notification.Notification;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.ui.notification.NotificationDetailActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<Notification> notifications;
    private final Context context;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMessage, tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        holder.tvTitle.setText(notification.getNotificationType());
        holder.tvMessage.setText(notification.getMessage());

        // Cắt ngày từ datetime ISO (ví dụ: 2025-07-21T13:30:00)
        String date = notification.getNotificationDate();
        if (date != null && date.length() >= 10) {
            holder.tvTime.setText(date.substring(0, 10));
        } else {
            holder.tvTime.setText("");
        }

        // Hiển thị khác biệt nếu chưa đọc
        if (!notification.isReadStatus()) {
            holder.itemView.setAlpha(1.0f);
            holder.tvTitle.setTypeface(null, Typeface.BOLD);
        } else {
            holder.itemView.setAlpha(0.5f);
            holder.tvTitle.setTypeface(null, Typeface.NORMAL);
        }

        holder.itemView.setOnClickListener(v -> {
            // Đánh dấu là đã đọc nếu chưa đọc
            if (!notification.isReadStatus()) {
                ApiService api = ApiClient.getApiService();
                api.markNotificationAsRead(notification.getNotificationId())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.d("Adapter", "Before update: " + notification.isReadStatus());
                                    notification.setReadStatus(true);
                                    Log.d("Adapter", "After update: " + notification.isReadStatus());

                                    int adapterPosition = holder.getAdapterPosition();
                                    if (adapterPosition != RecyclerView.NO_POSITION) {
                                        notifyItemChanged(adapterPosition);
                                    }
                                } else {
                                    Log.e("Adapter", "Mark as read failed. Code: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("Adapter", "Network error: " + t.getMessage(), t);
                                Toast.makeText(context, "Failed to mark as read", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            // Mở màn hình chi tiết
            Intent intent = new Intent(context, NotificationDetailActivity.class);
            intent.putExtra("title", notification.getNotificationType());
            intent.putExtra("message", notification.getMessage());
            intent.putExtra("date", notification.getNotificationDate().substring(0, 10));
            intent.putExtra("type", notification.getNotificationType());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}
