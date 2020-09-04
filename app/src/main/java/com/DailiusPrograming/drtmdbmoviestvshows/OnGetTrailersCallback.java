package com.DailiusPrograming.drtmdbmoviestvshows;

import java.util.List;

public interface OnGetTrailersCallback {
    void onSuccess(List<TrailerRepositoryGetKey> trailersGetKey);
    void onError();
}
