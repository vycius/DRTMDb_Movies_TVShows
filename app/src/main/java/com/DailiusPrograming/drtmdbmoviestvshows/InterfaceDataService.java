package com.DailiusPrograming.drtmdbmoviestvshows;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceDataService {
    @GET("movie/popular")
    Call<MovieRepository> getMovies(
      @Query("api_key") String apiKey,
      @Query("language") String language,
      @Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieRepository> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/upcoming")
    Call<MovieRepository> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/now_playing")
    Call<MovieRepository> getNowPlayingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<GenreRepository> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language);

}

