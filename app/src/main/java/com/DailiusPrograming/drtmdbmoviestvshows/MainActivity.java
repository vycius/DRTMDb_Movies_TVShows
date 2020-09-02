package com.DailiusPrograming.drtmdbmoviestvshows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.List;



public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewList;
    private RecyclerView_Adapter customAdapter;

    private List<GenreRepositoryGetGenreNames> genreList;
    private RetrofitClientInstance retrofitClientInstance;

    private ProgressDialog progressDialog;

    private String sortBy = RetrofitClientInstance.POPULAR;

    private int currentPage = 1;
    private boolean fetchingMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tlb_Toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        retrofitClientInstance = RetrofitClientInstance.getRetrofitInstance();
        recyclerViewList = findViewById(R.id.rvwMovie_RecyclerView);

        setupOnScrollListener();
        getGenres();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sort) {
            showSortMenu();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        recyclerViewList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = linearLayoutManager.getItemCount();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!fetchingMovies) {
                        showMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    private void showMovies(int page) {
        fetchingMovies = true;
        retrofitClientInstance.showMovies(page, sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<MovieRepositoryGetMovieDetails> movies) {
                if (customAdapter == null) {
                    customAdapter = new RecyclerView_Adapter(movies, genreList, callback);
                    recyclerViewList.setAdapter(customAdapter);
                } else {
                    if (page == 1) {
                        customAdapter.clearMovies();
                    }
                    customAdapter.setMovieList(movies);
                }
                progressDialog.dismiss();
                currentPage = page;
                fetchingMovies = false;

                setTitle();
            }

            @Override
            public void onError() {
                errorToast();
            }
        });
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(MovieRepositoryGetMovieDetails movieDetails) {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra(MovieActivity.MOVIE_ID, movieDetails.getId());
            startActivity(intent);
        }
    };

    public void getGenres() {
         retrofitClientInstance.getGenres(new OnGetGenresCallback() {
             @Override
             public void onSuccess(List<GenreRepositoryGetGenreNames> genres) {
                 genreList = genres;
                 showMovies(currentPage);
             }

             @Override
             public void onError() {
                 errorToast();
             }
         });
    }

    private void errorToast() {
        progressDialog.dismiss();
        Toast.makeText(MainActivity.this, "Something went wrong...Please try later!",
                Toast.LENGTH_SHORT).show();
    }

    private void showSortMenu() {
        final PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort));

        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBy =  RetrofitClientInstance.POPULAR;
                        showMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        sortBy = RetrofitClientInstance.TOP_RATED;
                        showMovies(currentPage);
                        return true;
                    case R.id.upcoming:
                        sortBy = RetrofitClientInstance.UPCOMING;
                        showMovies(currentPage);
                        return true;
                    case R.id.now_playing:
                        sortBy = RetrofitClientInstance.NOW_PLAYING;
                        showMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            }
        });

        sortMenu.inflate(R.menu.menu_movies_sort);
        sortMenu.show();
    }

    private void setTitle() {
        switch (sortBy) {
            case RetrofitClientInstance.POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case RetrofitClientInstance.TOP_RATED:
                setTitle(getString(R.string.top_rated));
                break;
            case RetrofitClientInstance.UPCOMING:
                setTitle(getString(R.string.upcoming));
                break;
            case RetrofitClientInstance.NOW_PLAYING:
                setTitle(getString(R.string.now_playing));
                break;

        }
    }
}