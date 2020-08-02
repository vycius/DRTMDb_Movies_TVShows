package com.DailiusPrograming.drtmdbmoviestvshows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    List<MovieGsonArray_LVL2> movieList;
    RecyclerView_Adapter customAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = new ArrayList<>();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        RecyclerView recyclerView = findViewById(R.id.rvwMovie_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        customAdapter = new RecyclerView_Adapter(this, movieList);
        recyclerView.setAdapter(customAdapter);

        InterfaceDataService service = RetrofitClientInstance.getRetrofitInstance().create(InterfaceDataService.class);
        Call<MovieGsonMain_LVL1> call = service.getMovies("ab66c610c798caaec8c4d5d522c826fd");
        call.enqueue(new Callback<MovieGsonMain_LVL1>(){

            @Override
            public void onResponse(Call<MovieGsonMain_LVL1> call, Response<MovieGsonMain_LVL1> response) {
                progressDialog.dismiss();
                MovieGsonMain_LVL1 moviesGsonResponse = response.body();
                customAdapter.setMovieList(moviesGsonResponse.getMovies());
            }

            @Override
            public void onFailure(Call<MovieGsonMain_LVL1> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}