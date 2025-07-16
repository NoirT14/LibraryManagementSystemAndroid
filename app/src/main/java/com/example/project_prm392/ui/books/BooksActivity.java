package com.example.project_prm392.ui.books;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.data.model.Book;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.getBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> books = response.body();
                    Toast.makeText(BooksActivity.this, "Sách: " + books.size(), Toast.LENGTH_SHORT).show();
                    // TODO: Hiển thị books lên RecyclerView
                } else {
                    Log.e("API", "Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("API", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
