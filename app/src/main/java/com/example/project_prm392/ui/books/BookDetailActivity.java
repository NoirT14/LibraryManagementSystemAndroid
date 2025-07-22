package com.example.project_prm392.ui.books;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.project_prm392.R;
import com.example.project_prm392.data.model.BookDetailInfoResponse;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView imgCover;
    private TextView tvTitle, tvAuthor, tvDescription, tvStatus, tvLocation, tvGenre, tvPublisher, tvYear, tvISBN;
    private ApiService apiService;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        toolbar = findViewById(R.id.toolbar);
        setupToolbar();
        // Khởi tạo view
        imgCover = findViewById(R.id.imgCover);
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvDescription = findViewById(R.id.tvDescription);
        tvStatus = findViewById(R.id.tvStatus);
        tvLocation = findViewById(R.id.tvLocation);
        tvGenre = findViewById(R.id.tvGenre);
        tvPublisher = findViewById(R.id.tvPublisher);
        tvYear = findViewById(R.id.tvYear);
        tvISBN = findViewById(R.id.tvISBN);

        apiService = ApiClient.getApiService();

        // Lấy bookId từ Intent
        int bookId = getIntent().getIntExtra("bookId", -1);
        if (bookId != -1) {
            loadBookDetail(bookId);
        } else {
            Toast.makeText(this, "Không tìm thấy mã sách", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Book Detail");
        }
    }

    private void loadBookDetail(int bookId) {
        apiService.getBookById(bookId).enqueue(new Callback<BookDetailInfoResponse>() {
            @Override
            public void onResponse(Call<BookDetailInfoResponse> call, Response<BookDetailInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookDetailInfoResponse book = response.body();

                    tvTitle.setText(book.title);
                    tvAuthor.setText(joinAuthors(book.authorNames != null ? book.authorNames.values : Collections.emptyList()));
                    tvDescription.setText(book.description != null ? book.description : "(Không có mô tả)");
                    tvStatus.setText(book.bookStatus);
                    tvGenre.setText(book.categoryName != null ? book.categoryName : "(Không rõ thể loại)");
                    if (book.variants != null && book.variants.values != null && !book.variants.values.isEmpty()) {
                        BookDetailInfoResponse.Variant variant = book.variants.values.get(0);
                        tvPublisher.setText(variant.publisher);
                        tvISBN.setText(variant.isbn);
                        tvYear.setText(String.valueOf(variant.publicationYear));
                        tvLocation.setText(variant.location != null ? variant.location : "(Không rõ vị trí)");
                    } else {
                        tvLocation.setText("(Không rõ vị trí)");
                    }

                    // Load ảnh bìa
                    Glide.with(BookDetailActivity.this)
                            .load(book.coverImg)
                            .placeholder(R.drawable.ic_book_placeholder)
                            .into(imgCover);
                } else {
                    Toast.makeText(BookDetailActivity.this, "Không tải được chi tiết sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookDetailInfoResponse> call, Throwable t) {
                Toast.makeText(BookDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private String joinAuthors(java.util.List<String> authors) {
        if (authors == null || authors.isEmpty()) return "(Không rõ)";
        return android.text.TextUtils.join(", ", authors);
    }
}
