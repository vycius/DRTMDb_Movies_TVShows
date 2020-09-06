package com.DailiusPrograming.drtmdbmoviestvshows;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// GenresResponse
public class GenreRepository {
    @SerializedName("genres")
    @Expose
    private List<GenreRepositoryGetGenreNames> genres;

    public List<GenreRepositoryGetGenreNames> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreRepositoryGetGenreNames> genres) {
        this.genres = genres;
    }
}
