package com.example.project_prm392.ui.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.project_prm392.R;
import com.example.project_prm392.data.local.SharedPrefsHelper;
import com.example.project_prm392.data.model.book.BookVariantDto;
import com.example.project_prm392.data.model.reservation.ReserveBookDto;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.ui.books.BookDetailActivity;
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
    private Spinner spinnerVolume;

    private ApiService apiService;
    private SharedPrefsHelper prefs;
    private int bookId;

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
        ivBookThumbnail = findViewById(R.id.imgCover);
        spinnerVolume = findViewById(R.id.spinnerVolume);

        apiService = ApiClient.getApiService();
        prefs = new SharedPrefsHelper(this);

        bookId = getIntent().getIntExtra("bookId", -1);
        if (bookId == -1) {
            Toast.makeText(this, "Không tìm thấy sách cần đặt.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadUserInfo() {
        etUserName.setText(prefs.getFullName());
    }

    private void loadBookInfo() {
        apiService.getReserveBookById(bookId).enqueue(new Callback<ReserveBookDto>() {
            @Override
            public void onResponse(Call<ReserveBookDto> call, Response<ReserveBookDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReserveBookDto book = response.body();

                    // Set tiêu đề sách
                    tvBookTitle.setText(book.getTitle());

                    // Load ảnh bìa bằng Glide
                    Glide.with(ReservationDetailActivity.this)
                            .load(book.getCoverImg())
                            .placeholder(R.drawable.ic_book_placeholder)
                            .into(ivBookThumbnail);

                    // Set tên tác giả
                    List<String> authors = book.getAuthorNames();
                    tvAuthor.setText(authors != null ? String.join(", ", authors) : "Không rõ");

                    // Load volumes lên Spinner
                    List<ReserveBookDto.VolumeDto> volumes = book.getVolumes();
                    if (volumes != null && !volumes.isEmpty()) {
                        ArrayAdapter<ReserveBookDto.VolumeDto> adapter = new ArrayAdapter<>(
                                ReservationDetailActivity.this,
                                android.R.layout.simple_spinner_item,
                                volumes
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerVolume.setAdapter(adapter);
                    } else {
                        Toast.makeText(ReservationDetailActivity.this, "Không có tập nào để đặt trước", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    tvBookTitle.setText("Không xác định");
                    tvAuthor.setText("Không xác định");
                    Toast.makeText(ReservationDetailActivity.this, "Lỗi khi tải dữ liệu sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReserveBookDto> call, Throwable t) {
                tvBookTitle.setText("Lỗi tải sách");
                tvAuthor.setText("Lỗi mạng");
                Toast.makeText(ReservationDetailActivity.this, "Không kết nối được tới máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        btnReserve.setOnClickListener(v -> reserveBook());
    }

    private void reserveBook() {
        int userId = prefs.getUserId();

        ReserveBookDto.VolumeDto selectedVolume = (ReserveBookDto.VolumeDto) spinnerVolume.getSelectedItem();
        if (selectedVolume == null) {
            Toast.makeText(this, "Vui lòng chọn tập sách", Toast.LENGTH_SHORT).show();
            return;
        }

        int volumeId = selectedVolume.getVolumeId();

        apiService.createReservation(userId, volumeId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReservationDetailActivity.this, "✅ Đặt thành công", Toast.LENGTH_SHORT).show();
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
