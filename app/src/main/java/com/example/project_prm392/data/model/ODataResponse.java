package com.example.project_prm392.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ODataResponse<T> {
    @SerializedName("$values")
    public List<T> values;
}

