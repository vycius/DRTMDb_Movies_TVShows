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
    private RatingBar movieRating;
    private LinearLayout movieTrailers;
    private LinearLayout movieReviews;

    private int movieId;
    private RetrofitClientInstance retrofitClientInstance;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

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
    }

    public void showMovieDetails () {
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
                    Glide.with(MovieActivity.this)
                            .load(IMAGE_BASE_URL + movieDtls.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(movieBackdrop);
                }
                getTrailers(movieDtls);
                progressDialog.dismiss();
            }

            @Override
            public void onError() {
                errorToast();
                finish();
            }
        });
    }

    private void getGenres(final MovieRepositoryGetMovieDetails movie){
        retrofitClientInstance.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<GenreRepositoryGetGenreNames> genres) {
                if(movie.getGenres()!= null){
                    List<String> currentGenres = new ArrayList<>();
                    for (GenreRepositoryGetGenreNames genre: movie.getGenres()){
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

    private void getTrailers(MovieRepositoryGetMovieDetails movieDetails){
        retrofitClientInstance.getTrailers(movieDetails.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<TrailerRepositoryGetKey> trailersGetKey) {
                trailersLabel.setVisibility(View.VISIBLE);
                movieTrailers.removeAllViews();
                for(final TrailerRepositoryGetKey trailerKey: trailersGetKey){
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