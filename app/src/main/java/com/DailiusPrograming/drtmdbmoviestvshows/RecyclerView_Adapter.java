package com.DailiusPrograming.drtmdbmoviestvshows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.CustomViewHolder>{
    List<MovieGsonArray_LVL2> dataList;
    Context context;

    public RecyclerView_Adapter (Context context, List<MovieGsonArray_LVL2> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    public void setMovieList(List<MovieGsonArray_LVL2> movieList) {
        this.dataList = movieList;
        notifyDataSetChanged();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;

        public CustomViewHolder(View itemView){
            super(itemView);
            releaseDate = itemView.findViewById(R.id.txt_Item_Movie_Release_Date);
            title = itemView.findViewById(R.id.txt_Item_Movie_Title);
            //rating = itemView.findViewById(R.id.txt_Item_Movie_Rating);
        }
        public void bind(MovieGsonArray_LVL2 movies){
            releaseDate.setText(movies.getRelease_date().split("-")[0]);
            title.setText(movies.getOriginal_title());
            genres.setText("");
        }
    }



    @Override
    public RecyclerView_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView_Adapter.CustomViewHolder holder, int position) {
        //holder.bind(dataList.get(position));
        holder.releaseDate.setText(dataList.get(position).getRelease_date());
        holder.title.setText(dataList.get(position).getOriginal_title());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
