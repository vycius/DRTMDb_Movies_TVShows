package com.DailiusPrograming.drtmdbmoviestvshows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final String LANGUAGE = "en-US";
    private InterfaceDataService interfaceDataService;
    String API_KEY = BuildConfig.API_KEY;


    private List<MovieGsonArray_LVL2> movieList;
    private List<GenreGsonArray_LVL2> genreList;
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
        switch (item.getItemId()) {
            case R.id.sort:
                showSortMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showMovies(final int page) {
        fetchingMovies = true;
        service.getMovies(API_KEY, LANGUAGE, page)
                .enqueue(new Callback<MovieGsonMain_LVL1>() {
                    @Override
                    public void onResponse(Call<MovieGsonMain_LVL1> call,
                                           Response<MovieGsonMain_LVL1> response) {
                        if (customAdapter == null){
                            customAdapter = new RecyclerView_Adapter(movieList, genreList);
                            recyclerViewList.setAdapter(customAdapter);
                            progressDialog.dismiss();

                            responceBodyAndAdapterSetMovies(response);

                        }else{
                            if(page == 1){
                                customAdapter.clearMovies();
                            }
                            responceBodyAndAdapterSetMovies(response);
                        }
                        currentPage = page;
                        fetchingMovies = false;
                    }

                    @Override
                    public void onFailure(Call<MovieGsonMain_LVL1> call, Throwable t) {
                        errorToast();
                    }
                });
    }

    public void getGenres(){
        service.getGenres(API_KEY, LANGUAGE)
                .enqueue(new Callback<GenreGsonMain_LVL1>() {
                    @Override
                    public void onResponse(Call<GenreGsonMain_LVL1> call,
                                           Response<GenreGsonMain_LVL1> response) {
                        if(response.isSuccessful()){
                            progressDialog.dismiss();
                            GenreGsonMain_LVL1 genreGsonMain_lvl1 = response.body();
                            if(genreGsonMain_lvl1 != null && genreGsonMain_lvl1.getGenres() != null){
                                genreList = genreGsonMain_lvl1.getGenres();
                                showMovies(currentPage);
                            }else{
                                errorToast();
                            }
                        }else {
                            errorToast();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenreGsonMain_LVL1> call, Throwable t) {
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
                        showMovies(currentPage + 1);
                    }
                }
            }
        });


    }
    private void responceBodyAndAdapterSetMovies(Response<MovieGsonMain_LVL1> response){
        MovieGsonMain_LVL1 moviesGsonResponse = response.body();
        customAdapter.setMovieList(moviesGsonResponse.getMovies());
    }


}