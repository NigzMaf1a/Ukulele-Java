package com.example.theukuleleband.modules.band;

public class BandBookingModel {
    private int BookingID;
    private String Genre;
    private String BookingDate;
    private String BookStatus;

    public BandBookingModel(int bookingID, String genre, String bookingDate, String bookStatus) {
        this.BookingID = bookingID;
        this.Genre = genre;
        this.BookingDate = bookingDate;
        this.BookStatus = bookStatus;
    }

    public int getBookingID() { return BookingID; }
    public String getGenre() { return Genre; }
    public String getBookingDate() { return BookingDate; }
    public String getBookStatus() { return BookStatus; }
}
