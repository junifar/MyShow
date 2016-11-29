package com.rubahapi.myshow;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.rubahapi.myshow.adapter.VideoAdapter;
import com.rubahapi.myshow.data.MovieDBHelper;
import com.rubahapi.myshow.data.MovieProvider;
import com.rubahapi.myshow.listener.OnVideoClickListener;
import com.rubahapi.myshow.service.VideoService;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnVideoClickListener {

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_MOVIE_ID = "MOVIE_ID";
    public static final int LOADER_RAMALAN_DETAIL = 200;
    public static final int LOADER_VIDEO = 300;
    private int ID;
    private int MOVIE_ID;
    public static final String[] VIDEO_COLUMNS = {
            MovieDBHelper.TABLE_VIDEOS_NAME + "." + MovieDBHelper.COLUMN_VIDEO_ID,
            MovieDBHelper.COLUMN_VIDEO_NAME,
            MovieDBHelper.COLUMN_VIDEO_KEY
    };
    VideoAdapter mVideoAdapter ;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        this.ID = getIntent().getIntExtra(EXTRA_ID, 0);
        this.MOVIE_ID = getIntent().getIntExtra(EXTRA_MOVIE_ID,0);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie_detail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MovieDetail.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mVideoAdapter = new VideoAdapter(null, this, this);
        mRecyclerView.setAdapter(mVideoAdapter);

        getLoaderManager().initLoader(LOADER_RAMALAN_DETAIL, null, this);
        getLoaderManager().initLoader(LOADER_VIDEO, null, this);

        startVideoService(this.MOVIE_ID);
    }

    private void startVideoService(int id){
        Intent videoService = new Intent(this, VideoService.class);
        videoService.putExtra("ID", id);
        startService(videoService);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri;
        switch (i){
            case LOADER_RAMALAN_DETAIL:
                uri = Uri.parse("content://" + MovieProvider.CONTENT_AUTHORITY + "/movie");
                return new CursorLoader(MovieDetail.this,
                        uri,
                        LatestMovieActivity.MOVIE_COLUMNS,null,null,null);
            case LOADER_VIDEO:
                uri = Uri.parse("content://com.rubahapi/video");
                return  new CursorLoader(MovieDetail.this,
                        uri,
                        MovieDetail.VIDEO_COLUMNS,
                        "VIDEOS.MOVIE_ID = ?",
                        new String[]{String.valueOf(this.MOVIE_ID)},
                        null);
        }
        return null;
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i("LOADER", String.valueOf(loader.getId()));
        switch (loader.getId()){
            case LOADER_RAMALAN_DETAIL:
                ImageView imageView = (ImageView) findViewById(R.id.imageViewPoster);
                TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
                TextView textViewYear = (TextView) findViewById(R.id.textViewYear);
                TextView textViewDuration = (TextView) findViewById(R.id.textViewDuration);
                TextView textViewDescription = (TextView) findViewById(R.id.textViewDescription);
                TextView textViewRating = (TextView) findViewById(R.id.textViewRating);

                cursor.moveToPosition(this.ID);
//        textView.setText(cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_IMAGE_PATH)));

                Context context = imageView.getContext();
                Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_IMAGE_PATH))).into(imageView);


                String title = cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_TITLE));
                String years = getYears(cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_YEARS)));
                String duration = cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_DURATION));
                String description = cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_DESCRIPTION));
                String rating = cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_RATING));

                textViewTitle.setText(title);
                textViewYear.setText(years);
                textViewDuration.setText((duration == null) ? "0Min" :  duration);
                textViewRating.setText((rating == null) ? "Unknown" : rating);
                textViewDescription.setText(description);
                break;
            case LOADER_VIDEO:
                mVideoAdapter.UpdateResult(cursor);
                mVideoAdapter.notifyDataSetChanged();
                break;
        }


    }

    private String getYears(String value){
        return value.split("-")[0];
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onVideoClick(String key) {
        Uri uri = Uri.parse("https://www.youtube.com/watch?v="+key);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
