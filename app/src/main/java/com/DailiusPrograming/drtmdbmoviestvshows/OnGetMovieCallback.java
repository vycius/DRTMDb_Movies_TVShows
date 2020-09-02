package com.DailiusPrograming.drtmdbmoviestvshows;

public interface OnGetMovieCallback {
    void onSuccess(MovieRepositoryGetMovieDetails movieDtls);
    void onError();
}
