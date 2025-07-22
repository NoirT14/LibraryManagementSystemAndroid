package com.example.project_prm392.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookDetailInfoResponse {
    public int bookId;
    public String title;
    public String language;
    public String bookStatus;
    public String description;
    public String coverImg;
    public String categoryName;
    public AuthorNamesWrapper authorNames;
    public VariantsWrapper variants;

    public static class Variant {
        public int variantId;
        public String publisher;
        public String isbn;
        public int publicationYear;
        public String coverType;
        public String paperQuality;
        public int price;
        public String location;
    }

    public static class AuthorNamesWrapper {
        @SerializedName("$values")
        public List<String> values;
    }

    public static class VariantsWrapper {
        @SerializedName("$values")
        public List<Variant> values;
    }
}
