package com.DailiusPrograming.drtmdbmoviestvshows;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.CustomViewHolder> {
    private List<MovieGsonArray_LVL2> movieList;
    private List<GenreGsonArray_LVL2> allGenres;
    private String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w300";

    public RecyclerView_Adapter(List<MovieGsonArray_LVL2> dataList,
                                List<GenreGsonArray_LVL2> allGenres) {
        this.movieList = dataList;
        this.allGenres = allGenres;
    }

    public void setMovieList(List<MovieGsonArray_LVL2> movieList) {
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    public void clearMovies() {
        movieList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView_Adapter.CustomViewHolder holder, int position) {
        holder.bind(movieList.get(position));
        //holder.releaseDate.setText(movieList.get(position).getRelease_date());
        //holder.title.setText(movieList.get(position).getOriginal_title());
        //holder.rating.setText(String.valueOf(movieList.get(position).getRating()));
        //holder.genres.setText(getGenres(movieList.get(position).getGenreIds()));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;
        ImageView poster;

        public CustomViewHolder(View itemView) {
            super(itemView);
            releaseDate = itemView.findViewById(R.id.txt_Item_Movie_Release_Date);
            title = itemView.findViewById(R.id.txt_Item_Movie_Title);
            rating = itemView.findViewById(R.id.txt_Item_Movie_Rating);
            genres = itemView.findViewById(R.id.txt_Item_Movie_Genre);
            poster = itemView.findViewById(R.id.ivw_Item_Movie_Poster);
        }

        public void bind(MovieGsonArray_LVL2 movies) {
            releaseDate.setText(movies.getRelease_date().split("-")[0]);
            title.setText(movies.getTitle());
            rating.setText(String.valueOf(movies.getRating()));
            genres.setText(distGenres(movies.getGenreIds()));
            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movies.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(poster);
        }

        private String distGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (GenreGsonArray_LVL2 genre : allGenres) {
                    if (genre.getId() == genreId) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }

    }


}
