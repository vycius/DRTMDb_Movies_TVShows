package com.DailiusPrograming.drtmdbmoviestvshows;

import java.util.List;

public interface OnGetMoviesCallback {
    void onSuccess(int page, List<MovieRepositoryGetMovieDetails> movies);
    void onError();
}
