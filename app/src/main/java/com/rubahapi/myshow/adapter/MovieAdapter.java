package com.rubahapi.myshow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rubahapi.myshow.R;
import com.rubahapi.myshow.pojo.popular.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by prasetia on 11/24/2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    public static final int TYPE_HEADER = 0;

    public static final int TYPE_ITEM = 1;

    private List<Result> results;

    public MovieAdapter(List<Result> results) {
        this.results = results;
    }

    public void updateResult(List<Result> results){
        this.results = results;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.imageView.getContext();

        Result result = results.get(position);

        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + result.getPosterPath()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(null != results){
            return  results.size();
        }
        return 0;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }
}
