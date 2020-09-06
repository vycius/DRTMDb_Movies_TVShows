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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// Klasės pavadinimas neturėtų turėti underscoro _ taip pat jis turėtų kažką pasakyti apie tai kas čia bus viduje
// Pvz: MoviesRecyclerViewAdapter ar MoviesListAdapter ar dar kažką
public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.CustomViewHolder> {
    private List<MovieRepositoryGetMovieDetails> movieList;
    private List<GenreRepositoryGetGenreNames> allGenres;
    private OnMoviesClickCallback callback;

    public RecyclerView_Adapter(List<MovieRepositoryGetMovieDetails> movieList,
                                List<GenreRepositoryGetGenreNames> allGenres,
                                OnMoviesClickCallback callback) {
        this.movieList = movieList;
        this.allGenres = allGenres;
        this.callback = callback;
    }

    public void setMovieList(List<MovieRepositoryGetMovieDetails> movieList) {
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    public void clearMovies() {
        movieList.clear();
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public RecyclerView_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView_Adapter.CustomViewHolder holder, int position) {
        holder.bind(movieList.get(position));
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
        MovieRepositoryGetMovieDetails movieDetails;

        public CustomViewHolder(View itemView) {
            super(itemView);
            releaseDate = itemView.findViewById(R.id.txt_Item_Movie_Release_Date);
            title = itemView.findViewById(R.id.txt_Item_Movie_Title);
            rating = itemView.findViewById(R.id.txt_Item_Movie_Rating);
            genres = itemView.findViewById(R.id.txt_Item_Movie_Genre);
            poster = itemView.findViewById(R.id.ivw_Item_Movie_Poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movieDetails);
                }
            });
        }

        public void bind(MovieRepositoryGetMovieDetails movies) {
            releaseDate.setText(movies.getRelease_date().split("-")[0]);
            title.setText(movies.getTitle());
            rating.setText(String.valueOf(movies.getRating()));
            genres.setText(distGenres(movies.getGenreIds()));
            // Urlai išmėtyti po visą kodą. Geriausia būtų juos laikyti vienoje vietoje ir esant reikalui lengvai pakeisti
            String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w300";
            // Ne visi filmai turi :) Nesuhandlintas casas, kai poster yra null
            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movies.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(poster);
            this.movieDetails = movies;
        }

        private String distGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            // Yra galimybė super paprastai padaryti t.y per vieną eilutę, tačiau ten būtų visiškai naujas konceptas
            // Tad supaprastinant dabartinę versiją gali išvengti vieno for
            // Egzistuoja genreIds.contains() metodas, kuris patikrina ar yra liste toks itemas
            for (Integer genreId : genreIds) {
                for (GenreRepositoryGetGenreNames genre : allGenres) {
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
