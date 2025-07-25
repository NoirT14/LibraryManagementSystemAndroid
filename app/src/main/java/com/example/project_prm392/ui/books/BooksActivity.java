package com.example.project_prm392.ui.books;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.project_prm392.R;
import com.example.project_prm392.data.model.BookBasicInfoRespone;
import com.example.project_prm392.data.model.ODataResponse;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import com.example.project_prm392.ui.reservation.ReservationDetailActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksActivity extends AppCompatActivity {

    private LinearLayout bookListContainer;
    private ApiService apiService;

    private int currentPage = 0;
    private final int PAGE_SIZE = 10;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private ScrollView scrollView;
    private String currentKeyword = "";
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        toolbar = findViewById(R.id.toolbar);
        setupToolbar();
        bookListContainer = findViewById(R.id.bookListContainer);
        scrollView = findViewById(R.id.scrollView);
        EditText edtSearch = findViewById(R.id.edtSearch);

        apiService = ApiClient.getApiService();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                currentKeyword = s.toString().trim();

                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    currentPage = 0;
                    isLastPage = false;
                    bookListContainer.removeAllViews();
                    loadBooksWithKeyword(currentKeyword);
                };

                searchHandler.postDelayed(searchRunnable, 500);
            }
        });

        loadBooksWithKeyword(currentKeyword);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View view = scrollView.getChildAt(0);
            if (view != null) {
                int diff = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());
                if (diff <= 0 && !isLoading && !isLastPage) {
                    loadBooksWithKeyword(currentKeyword);
                }
            }
        });
    }
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Library System");
        }
    }
    private void loadBooksWithKeyword(String keyword) {
        isLoading = true;
        int skip = currentPage * PAGE_SIZE;

        String odataFilter = null;
        if (!keyword.isEmpty()) {
            // escape dấu nháy đơn nếu có
            keyword = keyword.replace("'", "''");
            odataFilter = "contains(tolower(Title),'" + keyword.toLowerCase() + "')";
        }

        apiService.getBooks(skip, PAGE_SIZE, odataFilter).enqueue(new Callback<ODataResponse<BookBasicInfoRespone>>() {
            @Override
            public void onResponse(Call<ODataResponse<BookBasicInfoRespone>> call, Response<ODataResponse<BookBasicInfoRespone>> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    List<BookBasicInfoRespone> books = response.body().values;
                    if (books.isEmpty()) {
                        isLastPage = true;
                        return;
                    }

                    for (BookBasicInfoRespone book : books) {
                        addBookView(book);
                    }

                    currentPage++;
                    if (books.size() < PAGE_SIZE) {
                        isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<BookBasicInfoRespone>> call, Throwable t) {
                isLoading = false;
            }
        });
    }

    private void addBookView(BookBasicInfoRespone book) {
        View bookView = LayoutInflater.from(this).inflate(R.layout.book_card_item, bookListContainer, false);

        ImageView image = bookView.findViewById(R.id.imgCover);
        TextView title = bookView.findViewById(R.id.txtTitle);
        TextView author = bookView.findViewById(R.id.txtAuthor);
        Button btnBorrow = bookView.findViewById(R.id.btnBorrow);
        Button btnDetail = bookView.findViewById(R.id.btnDetail);

        title.setText(book.title);
        author.setText(book.author);
        Glide.with(this).load(book.coverImg).into(image);

        // Accessibility: cho trình đọc biết đây là 1 cuốn sách
        String speakableText = "Sách: " + book.title + ", Tác giả: " + book.author;
        bookView.setContentDescription(speakableText);

        btnBorrow.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReservationDetailActivity.class);
            intent.putExtra("bookId", book.bookId); // truyền ID sang
            startActivity(intent);
        });

        btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra("bookId", book.bookId); // truyền ID sang
            startActivity(intent);
        });


        bookListContainer.addView(bookView);
    }
}