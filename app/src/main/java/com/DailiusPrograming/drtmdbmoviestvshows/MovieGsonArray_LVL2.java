package com.DailiusPrograming.drtmdbmoviestvshows;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieGsonArray_LVL2 {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("original_title")
    @Expose
    private String original_title;
    @SerializedName("release_date")
    @Expose
    private String release_date;
    @SerializedName("vote_average")
    @Expose
    private float rating;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds;


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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }
}
