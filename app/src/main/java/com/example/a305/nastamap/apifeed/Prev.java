package com.example.a305.nastamap.apifeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prev {

    @SerializedName("href")
    @Expose
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}