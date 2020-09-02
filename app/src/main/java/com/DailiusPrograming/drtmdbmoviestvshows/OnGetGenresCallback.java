package com.DailiusPrograming.drtmdbmoviestvshows;

import java.util.List;

public interface OnGetGenresCallback {
    void onSuccess(List<GenreRepositoryGetGenreNames> genres);
    void onError();
}
