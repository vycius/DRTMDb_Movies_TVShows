package com.DailiusPrograming.drtmdbmoviestvshows;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// Tiesiog Movie ar MovieDetails
public class MovieRepositoryGetMovieDetails {
    @SerializedName("id")
    // Tavo atveju Expose niekur nėra reikalinga. Pašalinčiau iš visur jį. Tačiau nėra problemos jei jis ir yra.
    // https://stackoverflow.com/questions/34752200/gson-expose-vs-serializedname
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("release_date")
    @Expose
    // releaseDate be _
    private String release_date;
    @SerializedName("vote_average")
    @Expose
    private float rating;
    @SerializedName("poster_path")
    @Expose
    @Nullable
    private String posterPath;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("backdrop_path")
    @Expose
    @Nullable
    private String backdrop;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds;
    @SerializedName("genres")
    @Expose
    private List<GenreRepositoryGetGenreNames> genres;


    // Tai kas nenaudojama trink lauk
    public MovieRepositoryGetMovieDetails(Integer id, String title, String release_date) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public List<GenreRepositoryGetGenreNames> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreRepositoryGetGenreNames> genres) {
        this.genres = genres;
    }
}
