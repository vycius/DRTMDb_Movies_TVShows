package com.DailiusPrograming.drtmdbmoviestvshows;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";
    public static final String NOW_PLAYING = "now_playing";

    private static final String BASE_URL =
            "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    public static String API_KEY = BuildConfig.API_KEY;
    private InterfaceDataService itfApi;
    private static RetrofitClientInstance retrofitClientInstance;

    private RetrofitClientInstance(InterfaceDataService itfApi){
        this.itfApi = itfApi;
    }


    public static RetrofitClientInstance getRetrofitInstance() {
        if (retrofitClientInstance == null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            retrofitClientInstance = new RetrofitClientInstance(retrofit.create(InterfaceDataService.class));

        }
        return retrofitClientInstance;
    }


    public void getMovieDtls(int movieId, final OnGetMovieCallback callback){
        itfApi.getMovie(movieId, API_KEY, LANGUAGE)
                .enqueue(new Callback<MovieRepositoryGetMovieDetails>() {
                    @Override
                    public void onResponse(@NotNull Call<MovieRepositoryGetMovieDetails> call, @NotNull Response<MovieRepositoryGetMovieDetails> response) {
                        if(response.isSuccessful()){
                            MovieRepositoryGetMovieDetails movieDtls = response.body();
                            callback.onSuccess(movieDtls);
                        }
                    }
                    @Override
                    public void onFailure(@NotNull Call<MovieRepositoryGetMovieDetails> call, @NotNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getGenres(final OnGetGenresCallback callback){
        itfApi.getGenres(API_KEY, LANGUAGE)
                .enqueue(new Callback<GenreRepository>() {
            @Override
            public void onResponse(@NotNull Call<GenreRepository> call, @NotNull Response<GenreRepository> response) {
                if(response.isSuccessful()){
                    GenreRepository genreRepository = response.body();
                    assert genreRepository != null;
                    callback.onSuccess(genreRepository.getGenres());
                }else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(@NotNull Call<GenreRepository> call, @NotNull Throwable t) {
                callback.onError();
            }
        });
    }

    public void showMovies (int page, String sortBy, final OnGetMoviesCallback callback){
        Callback<MovieRepository> call = new Callback<MovieRepository>() {
            @Override
            public void onResponse(@NotNull Call<MovieRepository> call, Response<MovieRepository> response) {
                if (response.isSuccessful()){
                    MovieRepository movieRepository = response.body();
                    assert movieRepository != null;
                    callback.onSuccess(movieRepository.getPage(), movieRepository.getMovies());

                }else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MovieRepository> call, @NotNull Throwable t) {
                callback.onError();
            }
        };
        switch (sortBy){
            case TOP_RATED:
                itfApi.getTopRatedMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case UPCOMING:
                itfApi.getUpcomingMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case POPULAR:
                itfApi.getPopularMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case NOW_PLAYING:
                itfApi.getNowPlayingMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
        }

    }

    public void getTrailers(int movieId, final OnGetTrailersCallback callback){
        itfApi.getTrailers(movieId,API_KEY, LANGUAGE)
                .enqueue(new Callback<TrailerRepository>() {
                    @Override
                    public void onResponse(@NotNull Call<TrailerRepository> call, @NotNull Response<TrailerRepository> response) {
                        if(response.isSuccessful()){
                            TrailerRepository trailerRepository = response.body();
                            if(trailerRepository != null && trailerRepository.getTrailers() != null){
                                callback.onSuccess(trailerRepository.getTrailers());
                            }else {
                                callback.onError();
                            }
                        }else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<TrailerRepository> call, @NotNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getReviews (int movieId, final OnGetReviewsCallback callback){
        itfApi.getReviews(movieId, API_KEY, LANGUAGE)
                .enqueue(new Callback<ReviewRepository>() {
                    @Override
                    public void onResponse(@NotNull Call<ReviewRepository> call, @NotNull Response<ReviewRepository> response) {
                        if(response.isSuccessful()){
                            ReviewRepository reviewRepository =response.body();
                            if(reviewRepository != null && reviewRepository.getReview() != null){
                                callback.onSuccess(reviewRepository.getReview());
                            }else{
                                callback.onError();
                            }
                        }else{
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ReviewRepository> call, @NotNull Throwable t) {
                        callback.onError();
                    }
                });
    }


}
