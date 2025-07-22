package com.example.project_prm392.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.project_prm392.R;
import com.example.project_prm392.data.local.SharedPrefsHelper;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.ui.auth.LoginActivity;
import com.example.project_prm392.ui.books.BooksActivity;
import com.example.project_prm392.ui.loan.LoanListActivity;
import com.example.project_prm392.ui.notification.NotificationsActivity;
import com.example.project_prm392.ui.reservation.ReservationDetailActivity;
import com.example.project_prm392.ui.user.UserProfileActivity;
import com.example.project_prm392.utils.AuthManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome, tvUserInfo;
    private CardView cardProfile, cardBooks;
    private Toolbar toolbar;

    private SharedPrefsHelper sharedPrefsHelper;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initServices();
        checkAuthStatus();
        setupUserInterface();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        cardProfile = findViewById(R.id.cardProfile);
        cardBooks = findViewById(R.id.cardBooks);

        setSupportActionBar(toolbar);
    }

    private void initServices() {
        sharedPrefsHelper = new SharedPrefsHelper(this);
        authManager = new AuthManager(this);
    }

    private void checkAuthStatus() {
        if (!authManager.isLoggedIn()) {
            navigateToLogin();
            finish();
        }
    }

    private void setupUserInterface() {
        String fullName = sharedPrefsHelper.getFullName();
        String username = sharedPrefsHelper.getUsername();
        String role = authManager.getUserRole();

        tvWelcome.setText("Welcome, " + (fullName != null ? fullName : username));
        tvUserInfo.setText("Role: " + role + " | Username: " + username);
    }

    private void setupClickListeners() {
        cardProfile.setOnClickListener(v -> navigateToProfile());
        cardBooks.setOnClickListener(v -> navigateToBooks());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_notification);
        View actionView = item.getActionView();
        Log.d("DEBUG", "actionView: " + item.getActionView()); // kiểm tra có null không

        if (actionView == null) {
            Log.e("MainActivity", "❌ actionView is null! Check if menu_notification_badge.xml exists, and android:actionLayout is set correctly in menu_main.xml");
            return true;
        }

        TextView textBadge = actionView.findViewById(R.id.text_badge);
        ImageView icon = actionView.findViewById(R.id.icon_notification);

        if (textBadge == null || icon == null) {
            Log.e("MainActivity", "❌ Cannot find text_badge or icon_notification in layout. Check IDs in menu_notification_badge.xml");
            return true;
        }

        int userId = sharedPrefsHelper.getUserId();
        Log.d("DEBUG", "Current userId = " + userId);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getUnreadNotificationCount(userId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int count = response.body();
                    if (count > 0) {
                        textBadge.setText(String.valueOf(count));
                        textBadge.setVisibility(View.VISIBLE);
                    } else {
                        textBadge.setVisibility(View.GONE);
                    }
                } else {
                    Log.w("MainActivity", "⚠️ Unread count response not successful");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("MainActivity", "❌ Failed to fetch unread count", t);
                textBadge.setVisibility(View.GONE);
            }
        });

        // Click badge hoặc icon chuyển đến NotificationActivity
        actionView.setOnClickListener(v -> navigateToNotifications());

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            navigateToProfile();
            return true;
        } else if (id == R.id.action_books) {
            navigateToBooks();
            return true;
        } else if (id == R.id.action_notification) {
            navigateToNotifications();
            return true;
        } else if (id == R.id.action_logout) {
            showLogoutConfirmation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("No", null)
                .show();
    }

    private void performLogout() {
        authManager.logout(new AuthManager.LogoutCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Logout error: " + error, Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                    finish();
                });
            }
        });
    }

    private void navigateToProfile() {
        startActivity(new Intent(this, UserProfileActivity.class));
    }

    private void navigateToBooks() {
//        Intent intent = new Intent(this, LoanListActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(this, ReservationDetailActivity.class);
        intent.putExtra("variantId", 4);
        startActivity(intent);

    }

    private void navigateToNotifications() {
        startActivity(new Intent(this, NotificationsActivity.class));
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
