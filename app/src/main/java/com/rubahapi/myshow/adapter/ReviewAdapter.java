package com.rubahapi.myshow.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rubahapi.myshow.MovieDetail;
import com.rubahapi.myshow.R;
import com.rubahapi.myshow.data.MovieDBHelper;
import com.rubahapi.myshow.listener.OnReviewClickListener;

/**
 * Created by prasetia on 11/29/2016.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    Cursor cursor;
    Context context;
    OnReviewClickListener onReviewClickListener;

    public ReviewAdapter(Cursor cursor, Context context, OnReviewClickListener onReviewClickListener) {
        this.cursor = cursor;
        this.context = context;
        this.onReviewClickListener = onReviewClickListener;
    }

    public void UpdateResult(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_review, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    private int getArrayPosition(String value){
        int i = 0;
        for (String reviewColumn : MovieDetail.REVIEW_COLUMNS){
            if(reviewColumn == value){
                return i;
            }
            i++;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(null != cursor){
            cursor.moveToPosition(position);
            holder.textViewAuthor.setText(cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_REVIEW_AUTHOR)));
            holder.textViewContent.setText(cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_REVIEW_CONTENT)));
            holder.textViewUrl.setText(cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_REVIEW_URL)));
        }
    }

    @Override
    public int getItemCount() {
        if (null != cursor){
            return  cursor.getCount();
        }

        return 0;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewAuthor;
        public TextView textViewContent;
        public TextView textViewUrl;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewAuthor = (TextView) itemView.findViewById(R.id.textViewAuthor);
            textViewContent = (TextView) itemView.findViewById(R.id.textViewContent);
            textViewUrl = (TextView) itemView.findViewById(R.id.textViewURL);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            cursor.moveToPosition(position);
            onReviewClickListener.onReviewClick(cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_REVIEW_URL)));
        }
    }
}
