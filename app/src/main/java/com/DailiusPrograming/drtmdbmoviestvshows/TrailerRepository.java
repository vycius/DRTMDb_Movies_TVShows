package com.DailiusPrograming.drtmdbmoviestvshows;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// Pavadinimas pvz: TrailersResponse
public class TrailerRepository {
    @SerializedName("results")
    @Expose
    private List<TrailerRepositoryGetKey> trailers;

    public List<TrailerRepositoryGetKey> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<TrailerRepositoryGetKey> trailers) {
        this.trailers = trailers;
    }
}
