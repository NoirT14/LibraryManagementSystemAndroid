package com.example.project_prm392.ui.reservation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.data.model.reservation.ReservationDto;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private final List<ReservationDto> reservations;

    public ReservationAdapter(List<ReservationDto> reservations) {
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        ReservationDto r = reservations.get(position);
        holder.tvBookTitle.setText(r.getBookTitle());
        holder.tvVolume.setText("Tập: " + r.getVolumeTitle());
        holder.tvDate.setText("Ngày đặt: " + r.getReservedDate());
        holder.tvStatus.setText("Trạng thái: " + r.getStatus());
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvVolume, tvDate, tvStatus;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvVolume = itemView.findViewById(R.id.tvVolume);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}

