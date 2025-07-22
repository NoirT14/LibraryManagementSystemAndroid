package com.example.project_prm392.ui.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.R;
import com.example.project_prm392.data.local.SharedPrefsHelper;
import com.example.project_prm392.data.model.book.BookVariantDto;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.ui.main.MainActivity;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationDetailActivity extends AppCompatActivity {

    private TextView tvBookTitle, tvAuthor, tvReserveMessage;
    private EditText etUserName;
    private Button btnReserve;
    private ImageView ivBookThumbnail;

    private ApiService apiService;
    private SharedPrefsHelper prefs;
    private int variantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_book);

        initViews();
        loadUserInfo();
        loadBookInfo();
        setupListeners();
    }

    private void initViews() {
        tvBookTitle = findViewById(R.id.tvBookTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        etUserName = findViewById(R.id.etUserName);
        tvReserveMessage = findViewById(R.id.tvReserveMessage);
        btnReserve = findViewById(R.id.btnReserve);
        ivBookThumbnail = findViewById(R.id.ivBookThumbnail); // Optional: hiển thị ảnh

        apiService = ApiClient.getApiService();
        prefs = new SharedPrefsHelper(this);

        variantId = getIntent().getIntExtra("variantId", -1);
        if (variantId == -1) {
            Toast.makeText(this, "Không tìm thấy sách cần đặt.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadUserInfo() {
        etUserName.setText(prefs.getFullName());
    }

    private void loadBookInfo() {
        apiService.getBookVariantById(variantId).enqueue(new Callback<BookVariantDto>() {
            @Override
            public void onResponse(Call<BookVariantDto> call, Response<BookVariantDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookVariantDto book = response.body();
                    tvBookTitle.setText(book.getBookTitle());
                    List<String> authors = book.getAuthors();
                    tvAuthor.setText(authors != null ? String.join(", ", authors) : "Không rõ");
                } else {
                    tvBookTitle.setText("Không xác định");
                    tvAuthor.setText("Không xác định");
                }
            }

            @Override
            public void onFailure(Call<BookVariantDto> call, Throwable t) {
                tvBookTitle.setText("Lỗi tải sách");
                tvAuthor.setText("Lỗi mạng");
            }
        });
    }

    private void setupListeners() {
        btnReserve.setOnClickListener(v -> reserveBook());
    }

    private void reserveBook() {
        int userId = prefs.getUserId();
        apiService.createReservation(userId, variantId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReservationDetailActivity.this, "✅ Đặt giữ thành công", Toast.LENGTH_SHORT).show();
                    navigateHome();
                } else {
                    Toast.makeText(ReservationDetailActivity.this, "❌ Lỗi đặt giữ: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ReservationDetailActivity.this, "⚠️ Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear stack
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
