package com.DailiusPrograming.drtmdbmoviestvshows;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenreGsonMain_LVL1 {
    @SerializedName("genres")
    @Expose
    private List<GenreGsonArray_LVL2> genres;

    public List<GenreGsonArray_LVL2> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreGsonArray_LVL2> genres) {
        this.genres = genres;
    }
}
