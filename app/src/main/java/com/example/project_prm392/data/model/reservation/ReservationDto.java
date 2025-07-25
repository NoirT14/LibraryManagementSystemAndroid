package com.example.project_prm392.data.model.reservation;

public class ReservationDto {
    private int reservationId;
    private String bookTitle;
    private String volumeTitle;
    private String reservedDate;
    private String status;

    // Getters and Setters
    public int getReservationId() { return reservationId; }
    public String getBookTitle() { return bookTitle; }
    public String getVolumeTitle() { return volumeTitle; }
    public String getReservedDate() { return reservedDate; }
    public String getStatus() { return status; }

    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public void setVolumeTitle(String volumeTitle) { this.volumeTitle = volumeTitle; }
    public void setReservedDate(String reservedDate) { this.reservedDate = reservedDate; }
    public void setStatus(String status) { this.status = status; }
}

