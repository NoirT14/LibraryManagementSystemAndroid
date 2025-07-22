package com.example.project_prm392.ui.loan;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm392.R;
import com.example.project_prm392.data.local.SharedPrefsHelper;
import com.example.project_prm392.data.model.loan.Loan;
import com.example.project_prm392.data.remote.ApiClient;
import com.example.project_prm392.data.remote.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LoanAdapter loanAdapter;
    private ApiService apiService;
    private static final String TAG = "LoanListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_list);

        recyclerView = findViewById(R.id.recyclerViewLoans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiClient.getApiService();

        int userId = new SharedPrefsHelper(this).getUserId();
        fetchLoans(userId);
    }

    private void fetchLoans(int userId) {
        apiService.getLoansByUserId(userId).enqueue(new Callback<List<Loan>>() {
            @Override
            public void onResponse(Call<List<Loan>> call, Response<List<Loan>> response) {
                if (response.isSuccessful()) {
                    List<Loan> loans = response.body();
                    loanAdapter = new LoanAdapter(loans);
                    recyclerView.setAdapter(loanAdapter);
                } else {
                    Toast.makeText(LoanListActivity.this, "Failed to load loans", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Loan>> call, Throwable t) {
                Log.e(TAG, "Error fetching loans", t);
                Toast.makeText(LoanListActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
