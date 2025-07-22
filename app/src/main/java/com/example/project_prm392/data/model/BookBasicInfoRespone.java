package com.example.project_prm392.data.model;

import com.google.gson.annotations.SerializedName;

public class BookBasicInfoRespone {

    @SerializedName("bookId")
    public int bookId;

    @SerializedName("title")
    public String title;

    @SerializedName("author")
    public String author;

    @SerializedName("availability")
    public String availability;

    @SerializedName("coverImg")
    public String coverImg;
}

