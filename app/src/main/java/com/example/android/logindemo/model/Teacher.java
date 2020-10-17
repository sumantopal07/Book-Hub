package com.example.android.logindemo.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Teacher implements Serializable {
    private String name;
    private String imageURL, price, contactDetails;
    private String key;
    private String description;
    private String googlePay;
    private String pin;
    private String uniqueId;
    private int position;

    public Teacher() {
    }

    public Teacher(int position) {
        this.position = position;
    }

    public Teacher(String name, String price, String contactDetails, String imageUrl, String Des, String google, String pin, String uniqueId) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        this.imageURL = imageUrl;
        this.price = price;
        this.contactDetails = contactDetails;
        this.description = Des;
        this.googlePay = google;
        this.pin = pin;
        this.uniqueId = uniqueId;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getGooglePay() {
        return googlePay;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setGooglePayTex() {
        this.googlePay = googlePay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageURL;
    }

    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
