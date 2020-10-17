package com.example.android.logindemo.model;


public class SellerDetails {
    public String bookName;
    public String bookDescription;
    public String bookPrice;
    public String bookContactDetails;

    public SellerDetails(String bookName, String bookDescription, String bookPrice, String bookContactDetails) {
        this.bookName = bookName;
        this.bookDescription = bookDescription;
        this.bookPrice = bookPrice;
        this.bookContactDetails = bookContactDetails;
    }
}