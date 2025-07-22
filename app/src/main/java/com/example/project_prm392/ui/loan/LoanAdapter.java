package com.example.project_prm392.ui.loan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm392.R;
import com.example.project_prm392.data.model.loan.Loan;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {
    private List<Loan> loans;

    public LoanAdapter(List<Loan> loans) {
        this.loans = loans;
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan, parent, false);
        return new LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        Loan loan = loans.get(position);
        holder.tvTitle.setText(loan.getVolumeTitle());
        holder.tvVolume.setText("Vol. " + loan.getVolumeNumber());
        holder.tvBorrowDate.setText("Borrowed on " + loan.getBorrowDate());
        holder.tvDueDate.setText("Due on " + loan.getDueDate());
        holder.tvStatus.setText(loan.getLoanStatus());
    }

    @Override
    public int getItemCount() {
        return loans.size();
    }

    public static class LoanViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvVolume, tvBorrowDate, tvDueDate, tvStatus;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvVolume = itemView.findViewById(R.id.tvVolume);
            tvBorrowDate = itemView.findViewById(R.id.tvBorrowDate);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvStatus = itemView.findViewById(R.id.tvLoanStatus);
        }
    }
}
