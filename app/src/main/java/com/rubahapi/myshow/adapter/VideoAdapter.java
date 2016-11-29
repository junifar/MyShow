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
import com.rubahapi.myshow.data.VideoDBHelper;
import com.rubahapi.myshow.listener.OnVideoClickListener;

/**
 * Created by prasetia on 11/29/2016.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    Cursor cursor;
    Context context;
    OnVideoClickListener onVideoClickListener;

    public VideoAdapter(Cursor cursor, OnVideoClickListener onVideoClickListener, Context context) {
        this.cursor = cursor;
        this.context = context;
        this.onVideoClickListener = onVideoClickListener;
    }

    public void UpdateResult(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(null != cursor){
            cursor.moveToPosition(position);
            holder.textViewName.setText(cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_VIDEO_NAME)));
        }
    }

    private int getArrayPosition(String value){
        int i = 0;
        for (String videoColumn : MovieDetail.VIDEO_COLUMNS){
            if(videoColumn == value){
                return i;
            }
            i++;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if (null != cursor){
            return  cursor.getCount();
        }

        return 0;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewTrailerName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            cursor.moveToPosition(position);
            onVideoClickListener.onVideoClick(cursor.getString(getArrayPosition(VideoDBHelper.COLUMN_VIDEO_KEY)));
        }
    }
}
