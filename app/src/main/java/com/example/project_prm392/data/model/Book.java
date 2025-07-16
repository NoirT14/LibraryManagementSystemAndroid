package com.example.project_prm392.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Book {
    @SerializedName("bookId")
    public int bookId;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("language")
    public String language;

    @SerializedName("bookStatus")
    public String bookStatus;

    // Từ Book.Category.CategoryName
    @SerializedName("categoryName")
    public String categoryName;

    // Từ Book.Authors → List tên tác giả
    @SerializedName("authors")
    public List<String> authors;

    // Dữ liệu tính toán thêm từ DTO nếu có
    @SerializedName("totalCopies")
    public Integer totalCopies;

    @SerializedName("availableCopies")
    public Integer availableCopies;

    // Optional cho BookDetail
    @SerializedName("isbn")
    public String isbn;

    @SerializedName("publisherName")
    public String publisherName;

    @SerializedName("publicationYear")
    public Integer publicationYear;
}
