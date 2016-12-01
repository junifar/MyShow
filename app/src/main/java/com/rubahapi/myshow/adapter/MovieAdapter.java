package com.rubahapi.myshow.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rubahapi.myshow.LatestMovieActivity;
import com.rubahapi.myshow.R;
import com.rubahapi.myshow.data.MovieDBHelper;
import com.rubahapi.myshow.listener.OnMovieClickListener;
import com.squareup.picasso.Picasso;

/**
 * Created by prasetia on 11/24/2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Cursor cursor;
    OnMovieClickListener onMovieClickListener;
    Context context;

    public MovieAdapter(Cursor cursor, OnMovieClickListener onMovieClickListener, Context context) {
        this.cursor = cursor;
        this.onMovieClickListener = onMovieClickListener;
        this.context = context;
    }

    public void updateResult(Cursor cursor){
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    private int getArrayPosition(String value){
        int i = 0;
        for (String movieColumn : LatestMovieActivity.MOVIE_COLUMNS){
            if(movieColumn == value){
                return i;
            }
            i++;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.imageView.getContext();

        if(null != cursor){
            cursor.moveToPosition(position);
//            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//                Picasso.with(context).load("http://image.tmdb.org/t/p/w342" + cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_IMAGE_PATH))).into(holder.imageView);
//            }else{
//                Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_IMAGE_PATH))).into(holder.imageView);
//            }
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_IMAGE_PATH))).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {

        if (null != cursor){
            Log.i("CURSOR COUNT", String.valueOf(cursor.getCount()));
            return  cursor.getCount();
        }

        return 0;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            cursor.moveToPosition(position);
            onMovieClickListener.onMovieClick(position, cursor.getInt(getArrayPosition(MovieDBHelper.COLUMN_ID)));
        }
    }
}
