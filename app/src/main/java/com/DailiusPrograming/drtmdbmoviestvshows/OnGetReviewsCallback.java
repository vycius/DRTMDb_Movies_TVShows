package com.DailiusPrograming.drtmdbmoviestvshows;

import java.util.List;

public interface OnGetReviewsCallback {
    void onSuccess(List<ReviewRepositoryGetContent> review);
    void onError();
}
