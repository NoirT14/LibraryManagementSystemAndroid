package com.example.project_prm392.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WrappedList<T> {
    @SerializedName("$values")
    private List<T> values;

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }
}
