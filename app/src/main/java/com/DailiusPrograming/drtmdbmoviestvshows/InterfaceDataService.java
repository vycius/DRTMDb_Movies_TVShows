package com.DailiusPrograming.drtmdbmoviestvshows;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceDataService {
    @GET("movie/popular")
    Call<MovieGsonMain_LVL1> getMovies(
      @Query("api_key") String apiKey,
      @Query("language") String language,
      @Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieGsonMain_LVL1> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/upcoming")
    Call<MovieGsonMain_LVL1> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/now_playing")
    Call<MovieGsonMain_LVL1> getNowPlayingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<GenreGsonMain_LVL1> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language);

}

