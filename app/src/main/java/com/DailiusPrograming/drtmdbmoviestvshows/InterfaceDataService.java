package com.DailiusPrograming.drtmdbmoviestvshows;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceDataService {
    @GET("movie/popular")
    Call<MovieGsonMain_LVL1> getMovies(@Query("api_key") String apiKey);

    /**
     * @Query("api_key") String apiKey,
     * @Query("language") String language,
     * @Query("page") int page);
     */
}
