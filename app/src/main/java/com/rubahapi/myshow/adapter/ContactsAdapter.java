package com.rubahapi.myshow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rubahapi.myshow.R;
import com.rubahapi.myshow.pojo.popular.PopularMovie;
import com.rubahapi.myshow.pojo.popular.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by prasetia on 11/22/2016.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private PopularMovie mPopularMovies;
    private List<Result> results;
    private Context mContext;

    public ContactsAdapter(Context context, PopularMovie popularMovies) {
        mPopularMovies = popularMovies;
        mContext = context;
    }

    public void updateContacts(PopularMovie popularMovies){
        mPopularMovies = popularMovies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_contact, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
//        Contact contact = mContacts.get(position);
        results = mPopularMovies.getResults();
        Result result = results.get(position);

        ImageView imageView = holder.showImageView;
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + result.getPosterPath()).into(imageView);
    }

    @Override
    public int getItemCount(){
        return results.size();
    }

//    private Context getContext(){
//        return  mContext;
//    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView showImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            showImageView = (ImageView) itemView.findViewById(R.id.image_view);
        }

    }

}
