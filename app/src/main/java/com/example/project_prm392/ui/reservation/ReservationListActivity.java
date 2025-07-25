package com.example.project_prm392.ui.reservation;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.data.WrappedList;
import com.example.project_prm392.data.local.SharedPrefsHelper;
import com.example.project_prm392.data.model.reservation.ReservationDto;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationListActivity extends AppCompatActivity {

    private RecyclerView rvReservations;
    private ApiService apiService;
    private SharedPrefsHelper prefs;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);
        toolbar = findViewById(R.id.toolbar);
        setupToolbar();
        rvReservations = findViewById(R.id.rvReservations);
        rvReservations.setLayoutManager(new LinearLayoutManager(this));

        prefs = new SharedPrefsHelper(this);
        apiService = ApiClient.getApiService();

        int userId = prefs.getUserId();

        apiService.getReservationsByUser(userId).enqueue(new Callback<WrappedList<ReservationDto>>() {
            @Override
            public void onResponse(Call<WrappedList<ReservationDto>> call, Response<WrappedList<ReservationDto>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValues() != null) {
                    List<ReservationDto> reservations = response.body().getValues();
                    ReservationAdapter adapter = new ReservationAdapter(reservations);
                    rvReservations.setAdapter(adapter);
                } else {
                    Toast.makeText(ReservationListActivity.this, "Lỗi khi tải danh sách đặt giữ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedList<ReservationDto>> call, Throwable t) {
                Toast.makeText(ReservationListActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
}

