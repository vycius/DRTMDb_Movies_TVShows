package com.DailiusPrograming.drtmdbmoviestvshows;

import com.google.gson.annotations.SerializedName;

public class MovieGsonArray_LVL2 {
    @SerializedName("id")
    private Integer id;
    @SerializedName("original_title")
    private String original_title;
    @SerializedName("release_date")
    private String release_date;


    public MovieGsonArray_LVL2(Integer id, String original_title, String release_date) {
        this.id = id;
        this.original_title = original_title;
        this.release_date = release_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }


}
