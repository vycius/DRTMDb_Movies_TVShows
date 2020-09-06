package com.DailiusPrograming.drtmdbmoviestvshows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;


public class MovieActivity extends AppCompatActivity {
    public static String MOVIE_ID = "movie_id";

    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/0.jpg";

    private ImageView movieBackdrop;
    private TextView movieTitle;
    private TextView movieGenres;
    private TextView movieOverview;
    private TextView movieOverviewLabel;
    private TextView movieReleaseDate;
    private TextView trailersLabel;
    private TextView reviewsLabel;
    private RatingBar movieRating;

    // Nedaryk addView. Vietoj to RecyclerView su keliais view tipai. Komentaras žemiau :)
    private LinearLayout movieTrailers;
    private LinearLayout movieReviews;

    private int movieId;
    private RetrofitClientInstance retrofitClientInstance;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Siūlau patikrinti visada ar buvo paduotas duomuo ir jei ne tuomet mesti exception
        // Dabar jei leisdamas naują activity pamiršti paduoti movieId, tai jis kraus filmuką su id = 0
        // if(!getIntent().hasExtra(MOVIE_ID)) bla bla

        // O kas jei persiųstume filmą, o ne vien ID, kurį turėjome prieš tai buvusiame activity ?
        // Juk visa informacija jau buvo užkrauta.
        // Tokiu atveju nereikėtų loading stato filmui :)
        // https://medium.com/@gaandlaneeraja/how-to-pass-objects-between-android-activities-86f2cfb61bd4
        // Kolkas naudok Serializable, tačiau žinok, kad egzistuoja ir geresnis variantas Parcelable.
        // Tačiau Parcelable niekada nerašyk rankomis. Juos įmanoma sugeneruoti automatiškai :)
        movieId = getIntent().getIntExtra(MOVIE_ID, movieId);
        retrofitClientInstance = RetrofitClientInstance.getRetrofitInstance();

        progressDialog = new ProgressDialog(MovieActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        setupToolbar();

        initUI();

        showMovieDetails();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tlb_Toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initUI() {
        // Skaiciuotume jau buvau pradejes naudoti View bindingus
        // https://github.com/Dailius/Calculator/blob/master/app/src/main/java/com/android/developer/MainActivity.java#L30
        // Gal vertetu ir čia juos užnaudoti
        movieBackdrop = findViewById(R.id.ivw_Movie_Details_Backdrop);
        movieTitle = findViewById(R.id.txt_Movie_Details_Title);
        movieGenres = findViewById(R.id.txt_Movie_Details_Genres);
        movieOverview = findViewById(R.id.txt_Movie_Details_Overview);
        movieOverviewLabel = findViewById(R.id.lbl_Summary_Label);
        movieReleaseDate = findViewById(R.id.txt_Movie_Details_ReleaseDate);
        movieRating = findViewById(R.id.rbr_Movie_Details_Rating);
        movieTrailers = findViewById(R.id.lly_Movie_Trailers);
        movieReviews = findViewById(R.id.lly_Movie_Reviews);
        trailersLabel = findViewById(R.id.txt_Trailers_Label);
        reviewsLabel = findViewById(R.id.txt_Reviews_Label);
    }

    public void showMovieDetails() {
        retrofitClientInstance.getMovieDtls(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(MovieRepositoryGetMovieDetails movieDtls) {
                movieTitle.setText(movieDtls.getTitle());
                movieOverviewLabel.setVisibility(View.VISIBLE);
                movieOverview.setText(movieDtls.getOverview());
                movieRating.setVisibility(View.VISIBLE);
                movieRating.setRating(movieDtls.getRating() / 2);
                getGenres(movieDtls);
                movieReleaseDate.setText(movieDtls.getRelease_date());
                if (!isFinishing()) {
                    // Geriau susikurk papildomą funkciją movieDtls.getBackdropUrl() kuri gražina pilną
                    // backdropo pathą. Šitaip turėsi mažiau logikos activity ir jam nereiks žinoti
                    // apie IMAGE_BASE_URL. Tą patį gali padaryti su YOUTUBE_VIDEO_URL bei YOUTUBE_THUMBNAIL_URL

                    // Nesuhandlintas backdrop = null casas
                    // W/Glide: Load failed for https://image.tmdb.org/t/p/w780null with size [1080x704]
                    //    class com.bumptech.glide.load.engine.GlideException: Failed to load resource
                    //    There was 1 cause:
                    //    java.io.FileNotFoundException(https://image.tmdb.org/t/p/w780null)
                    //     call GlideException#logRootCauses(String) for more detail
                    //      Cause (1 of 1): class com.bumptech.glide.load.engine.GlideException: Fetching data failed, class java.io.InputStream, REMOTE
                    //    There was 1 cause:
                    //    java.io.FileNotFoundException(https://image.tmdb.org/t/p/w780null)
                    //     call GlideException#logRootCauses(String) for more detail
                    //        Cause (1 of 1): class com.bumptech.glide.load.engine.GlideException: Fetch failed
                    //    There was 1 cause:
                    //    java.io.FileNotFoundException(https://image.tmdb.org/t/p/w780null)
                    //     call GlideException#logRootCauses(String) for more detail
                    //          Cause (1 of 1): class java.io.FileNotFoundException: https://image.tmdb.org/t/p/w780null
                    //I/Glide: Root cause (1 of 1)
                    //    java.io.FileNotFoundException: https://image.tmdb.org/t/p/w780null
                    //        at com.android.okhttp.internal.huc.HttpURLConnectionImpl.getInputStream(HttpURLConnectionImpl.java:255)
                    //        at com.android.okhttp.internal.huc.DelegatingHttpsURLConnection.getInputStream(DelegatingHttpsURLConnection.java:211)
                    //        at com.android.okhttp.internal.huc.HttpsURLConnectionImpl.getInputStream(HttpsURLConnectionImpl.java:30)
                    //        at com.bumptech.glide.load.data.HttpUrlFetcher.loadDataWithRedirects(HttpUrlFetcher.java:102)
                    //        at com.bumptech.glide.load.data.HttpUrlFetcher.loadData(HttpUrlFetcher.java:56)
                    //        at com.bumptech.glide.load.model.MultiModelLoader$MultiFetcher.loadData(MultiModelLoader.java:100)
                    //        at com.bumptech.glide.load.model.MultiModelLoader$MultiFetcher.startNextOrFail(MultiModelLoader.java:164)
                    //        at com.bumptech.glide.load.model.MultiModelLoader$MultiFetcher.onLoadFailed(MultiModelLoader.java:154)
                    //        at com.bumptech.glide.load.data.HttpUrlFetcher.loadData(HttpUrlFetcher.java:62)
                    //        at com.bumptech.glide.load.model.MultiModelLoader$MultiFetcher.loadData(MultiModelLoader.java:100)
                    //        at com.bumptech.glide.load.engine.SourceGenerator.startNextLoad(SourceGenerator.java:70)
                    //        at com.bumptech.glide.load.engine.SourceGenerator.startNext(SourceGenerator.java:63)
                    //        at com.bumptech.glide.load.engine.DecodeJob.runGenerators(DecodeJob.java:310)
                    //        at com.bumptech.glide.load.engine.DecodeJob.runWrapped(DecodeJob.java:279)
                    //        at com.bumptech.glide.load.engine.DecodeJob.run(DecodeJob.java:234)
                    //        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
                    //        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
                    //        at java.lang.Thread.run(Thread.java:923)
                    //        at com.bumptech.glide.load.engine.executor.GlideExecutor$DefaultThreadFactory$1.run(GlideExecutor.java:393)
                    Glide.with(MovieActivity.this)
                            .load(IMAGE_BASE_URL + movieDtls.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(movieBackdrop);
                }
                getTrailers(movieDtls);
                progressDialog.dismiss();
                getReview(movieDtls);
            }

            @Override
            public void onError() {
                // Tiesiog žinutė apie klaidą rodoma useriui ? Labai kvailai atrodo, kai įvyksta erroras ir užsidaro activity
                errorToast();
                finish();
            }
        });
    }

    private void getGenres(final MovieRepositoryGetMovieDetails movie) {
        retrofitClientInstance.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<GenreRepositoryGetGenreNames> genres) {
                // Kokiu atveju jis bus nullas ?
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (GenreRepositoryGetGenreNames genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    movieGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                errorToast();
            }
        });
    }

    private void getTrailers(MovieRepositoryGetMovieDetails movieDetails) {
        retrofitClientInstance.getTrailers(movieDetails.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<TrailerRepositoryGetKey> trailersGetKey) {
                trailersLabel.setVisibility(View.VISIBLE);
                // Super nepatinka man šita vieta su removeAllViews ir addView
                // Kurkas geriau būtų naudoti RecyclerView su keletų view tipų
                // Plačiau https://medium.com/@droidbyme/android-recyclerview-with-multiple-view-type-multiple-view-holder-af798458763b
                movieTrailers.removeAllViews();
                for (final TrailerRepositoryGetKey trailerKey : trailersGetKey) {
                    View parent = getLayoutInflater().inflate(R.layout.thumbnail_trailer, movieTrailers, false);
                    ImageView thumbnail = parent.findViewById(R.id.thumbnail);
                    thumbnail.requestLayout();
                    thumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTrailer(String.format(YOUTUBE_VIDEO_URL, trailerKey.getKey()));
                        }
                    });
                    Glide.with(MovieActivity.this)
                            .load(String.format(YOUTUBE_THUMBNAIL_URL, trailerKey.getKey()))
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                            .into(thumbnail);
                    movieTrailers.addView(parent);
                }
            }

            @Override
            public void onError() {
                trailersLabel.setVisibility(View.GONE);
            }
        });
    }

    private void showTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void getReview(final MovieRepositoryGetMovieDetails movieDetails) {
        retrofitClientInstance.getReviews(movieDetails.getId(), new OnGetReviewsCallback() {
            @Override
            public void onSuccess(List<ReviewRepositoryGetContent> review) {
                reviewsLabel.setVisibility(View.VISIBLE);
                movieReviews.removeAllViews();
                for (ReviewRepositoryGetContent reviewDetails : review) {
                    View parent = getLayoutInflater().inflate(R.layout.review, movieReviews, false);
                    TextView author = parent.findViewById(R.id.reviewAuthor);
                    TextView content = parent.findViewById(R.id.reviewContent);
                    author.setText(reviewDetails.getAuthor());
                    content.setText(reviewDetails.getContent());
                    movieReviews.addView(parent);
                }
            }

            @Override
            public void onError() {
                // no actions
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void errorToast() {
        progressDialog.dismiss();
        Toast.makeText(MovieActivity.this, "Something went wrong...Please try later!",
                Toast.LENGTH_SHORT).show();
    }

}