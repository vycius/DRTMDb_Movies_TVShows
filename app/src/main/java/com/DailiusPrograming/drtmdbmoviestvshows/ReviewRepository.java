package com.DailiusPrograming.drtmdbmoviestvshows;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// Pavadinimas. Geriau pvz: MovieReviewsResponse ar ReviewsResponse ...
public class ReviewRepository {
    @SerializedName("results")
    @Expose
    private List<ReviewRepositoryGetContent> review;

    public List<ReviewRepositoryGetContent> getReview() {
        return review;
    }

    public void setReview(List<ReviewRepositoryGetContent> review) {
        this.review = review;
    }
}
