// MainActivity.java - Updated để access Profile
package com.example.project_prm392.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.example.project_prm392.R;
import com.example.project_prm392.ui.auth.LoginActivity;
import com.example.project_prm392.ui.books.BooksActivity;
import com.example.project_prm392.ui.user.UserProfileActivity;
import com.example.project_prm392.utils.AuthManager;
import com.example.project_prm392.data.local.SharedPrefsHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

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
            return;
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
                    // Still navigate to login on error
                    navigateToLogin();
                    finish();
                });
            }
        });
    }

    private void navigateToProfile() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void navigateToBooks() {
        Intent intent = new Intent(this, BooksActivity.class);
        startActivity(intent);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}