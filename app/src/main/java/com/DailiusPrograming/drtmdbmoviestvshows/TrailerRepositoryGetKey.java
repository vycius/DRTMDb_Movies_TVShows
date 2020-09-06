package com.DailiusPrograming.drtmdbmoviestvshows;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Pavadinimas :)
public class TrailerRepositoryGetKey {
    @SerializedName("key")
    @Expose
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
