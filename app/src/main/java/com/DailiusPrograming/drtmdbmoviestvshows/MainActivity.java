package com.DailiusPrograming.drtmdbmoviestvshows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final String LANGUAGE = "en-US";
    String API_KEY = BuildConfig.API_KEY;
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";
    public static final String NOW_PLAYING = "now_playing";
    private String sortBy = POPULAR;


    private List<MovieRepositoryGetMovieDetails> movieList;
    private List<GenreRepositoryGetGenreNames> genreList;
    private RecyclerView recyclerViewList;
    private RecyclerView_Adapter customAdapter;
    private ProgressDialog progressDialog;

    private InterfaceDataService service;
    private int currentPage =1;
    private boolean fetchingMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.tlb_Toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        recyclerViewList = findViewById(R.id.rvwMovie_RecyclerView);
        service = RetrofitClientInstance.getRetrofitInstance()
                .create(InterfaceDataService.class);

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

    public void showMovies(final int page, String sortBy) {
        fetchingMovies = true;
        Callback<MovieRepository> callMovies = new Callback<MovieRepository>() {
                    @Override
                    public void onResponse(@NotNull Call<MovieRepository> call,
                                           @NotNull Response<MovieRepository> response) {
                        if (customAdapter == null){
                            customAdapter = new RecyclerView_Adapter(movieList, genreList);
                            recyclerViewList.setAdapter(customAdapter);
                        }else{
                            if(page == 1){
                                customAdapter.clearMovies();
                            }
                        }
                        progressDialog.dismiss();
                        responceBodyAndAdapterSetMovies(response);
                        currentPage = page;
                        fetchingMovies = false;

                        setTitle();
                    }

                    @Override
                    public void onFailure(@NotNull Call<MovieRepository> call, @NotNull Throwable t) {
                        errorToast();
                    }
                };

        switch (sortBy) {
            case TOP_RATED:
                service.getTopRatedMovies(API_KEY, LANGUAGE, page)
                        .enqueue(callMovies);
                break;
            case UPCOMING:
                service.getUpcomingMovies(API_KEY, LANGUAGE, page)
                        .enqueue(callMovies);
                break;
            case POPULAR:
                service.getMovies(API_KEY, LANGUAGE, page)
                        .enqueue(callMovies);
                break;
            case NOW_PLAYING:
                service.getNowPlayingMovies(API_KEY, LANGUAGE, page)
                        .enqueue(callMovies);
                break;

        }


    }

    public void getGenres(){
        service.getGenres(API_KEY, LANGUAGE)
                .enqueue(new Callback<GenreRepository>() {
                    @Override
                    public void onResponse(@NotNull Call<GenreRepository> call,
                                           @NotNull Response<GenreRepository> response) {
                        if(response.isSuccessful()){
                            progressDialog.dismiss();
                            GenreRepository genreRepository = response.body();
                            if(genreRepository != null && genreRepository.getGenres() != null){
                                genreList = genreRepository.getGenres();
                                showMovies(currentPage, sortBy);
                            }else{
                                errorToast();
                            }
                        }else {
                            errorToast();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<GenreRepository> call, @NotNull Throwable t) {
                        errorToast();
                    }
                });
    }
    private void errorToast(){
        progressDialog.dismiss();
        Toast.makeText(MainActivity.this, "Something went wrong...Please try later!",
                Toast.LENGTH_SHORT).show();
    }

    private void showSortMenu(){
        final PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort));

        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                currentPage = 1;

                switch (item.getItemId()){
                    case R.id.popular:
                        sortBy = POPULAR;
                        showMovies(currentPage, sortBy);
                        return true;
                    case R.id.top_rated:
                        sortBy = TOP_RATED;
                        showMovies(currentPage, sortBy);
                        return true;
                    case R.id.upcoming:
                        sortBy = UPCOMING;
                        showMovies(currentPage, sortBy);
                        return true;
                    case R.id.now_playing:
                        sortBy = NOW_PLAYING;
                        showMovies(currentPage, sortBy);
                        return true;

                    default:
                        return false;
                }
            }
        });

        sortMenu.inflate(R.menu.menu_movies_sort);
        sortMenu.show();
    }

    private void setupOnScrollListener(){
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        recyclerViewList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = linearLayoutManager.getItemCount();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!fetchingMovies) {
                        showMovies(currentPage + 1, sortBy);
                    }
                }
            }
        });


    }
    private void responceBodyAndAdapterSetMovies(Response<MovieRepository> response){
        MovieRepository movieRepository = response.body();
        assert movieRepository != null;
        customAdapter.setMovieList(movieRepository.getMovies());
    }

    private void setTitle() {
        switch (sortBy) {
            case POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case TOP_RATED:
                setTitle(getString(R.string.top_rated));
                break;
            case UPCOMING:
                setTitle(getString(R.string.upcoming));
                break;
            case NOW_PLAYING:
                setTitle(getString(R.string.now_playing));
                break;

        }
    }


}