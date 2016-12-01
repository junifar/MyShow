package com.rubahapi.myshow;

import android.app.LoaderManager;
import android.content.ContentValues;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rubahapi.myshow.adapter.ReviewAdapter;
import com.rubahapi.myshow.adapter.VideoAdapter;
import com.rubahapi.myshow.data.MovieDBHelper;
import com.rubahapi.myshow.data.MovieProvider;
import com.rubahapi.myshow.listener.OnReviewClickListener;
import com.rubahapi.myshow.listener.OnVideoClickListener;
import com.rubahapi.myshow.service.ReviewService;
import com.rubahapi.myshow.service.VideoService;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnVideoClickListener, OnReviewClickListener {

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_MOVIE_ID = "MOVIE_ID";
    public static final int LOADER_RAMALAN_DETAIL = 200;
    public static final int LOADER_REVIEW = 400;
    public static final int LOADER_VIDEO = 300;
    private int ID;
    private int MOVIE_ID;
    public static final String[] VIDEO_COLUMNS = {
            MovieDBHelper.TABLE_VIDEOS_NAME + "." + MovieDBHelper.COLUMN_VIDEO_ID,
            MovieDBHelper.COLUMN_VIDEO_NAME,
            MovieDBHelper.COLUMN_VIDEO_KEY
    };
    public static final String[] REVIEW_COLUMNS = {
            MovieDBHelper.TABLE_REVIEW_NAME + "." + MovieDBHelper.COLUMN_REVIEW_ID,
            MovieDBHelper.COLUMN_REVIEW_AUTHOR,
            MovieDBHelper.COLUMN_REVIEW_CONTENT,
            MovieDBHelper.COLUMN_REVIEW_URL
    };
    VideoAdapter mVideoAdapter ;
    ReviewAdapter mReviewAdapter;
    RecyclerView mRecyclerView;
    RecyclerView mRecyclerViewReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        this.ID = getIntent().getIntExtra(EXTRA_ID, 0);
        this.MOVIE_ID = getIntent().getIntExtra(EXTRA_MOVIE_ID,0);

        Button btnFavourite = (Button) findViewById(R.id.buttonFavourite);

        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markAsFavourite();
                Toast.makeText(MovieDetail.this, "Saved as your favourites movies", Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie_detail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MovieDetail.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mVideoAdapter = new VideoAdapter(null, this, this);
        mRecyclerView.setAdapter(mVideoAdapter);

        mRecyclerViewReview = (RecyclerView) findViewById(R.id.recyclerview_review);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(MovieDetail.this, LinearLayoutManager.VERTICAL,false);
        mRecyclerViewReview.setLayoutManager(linearLayoutManager1);
        mReviewAdapter = new ReviewAdapter(null, this, this);
        mRecyclerViewReview.setAdapter(mReviewAdapter);

        getLoaderManager().initLoader(LOADER_RAMALAN_DETAIL, null, this);
        getLoaderManager().initLoader(LOADER_VIDEO, null, this);
        getLoaderManager().initLoader(LOADER_REVIEW, null, this);

        startVideoService(this.MOVIE_ID);
        startReviewService(this.MOVIE_ID);
    }

    private int markAsFavourite(){
        Uri uri = Uri.parse("content://com.rubahapi.favourite/favourite");

        Cursor movieData = getMovieList();
        movieData.moveToPosition(0);

        ContentValues cv = new ContentValues();
        cv.put(MovieDBHelper.COLUMN_FAVOURITE_MOVIE_ID, this.MOVIE_ID);
        cv.put(MovieDBHelper.COLUMN_TITLE, movieData.getString(getArrayPosition(MovieDBHelper.COLUMN_TITLE)));
        cv.put(MovieDBHelper.COLUMN_YEARS, movieData.getString(getArrayPosition(MovieDBHelper.COLUMN_YEARS)));
        cv.put(MovieDBHelper.COLUMN_DURATION, movieData.getString(getArrayPosition(MovieDBHelper.COLUMN_DURATION)));
        cv.put(MovieDBHelper.COLUMN_RATING, movieData.getString(getArrayPosition(MovieDBHelper.COLUMN_RATING)));
        cv.put(MovieDBHelper.COLUMN_DESCRIPTION, movieData.getString(getArrayPosition(MovieDBHelper.COLUMN_DESCRIPTION)));
        cv.put(MovieDBHelper.COLUMN_IMAGE_PATH, movieData.getString(getArrayPosition(MovieDBHelper.COLUMN_IMAGE_PATH)));
        getContentResolver().insert(uri,cv);
        getContentResolver().notifyChange(uri, null);
        return 1;
    }

    private Cursor getMovieList(){

        Uri uri = Uri.parse("content://" + MovieProvider.CONTENT_AUTHORITY + "/movie");

        return getContentResolver().query(uri,
                LatestMovieActivity.MOVIE_COLUMNS,
                "_id = ?",
                new String[]{String.valueOf(MOVIE_ID)},null);
    }

    private void startVideoService(int id){
        Intent videoService = new Intent(this, VideoService.class);
        videoService.putExtra("ID", id);
        startService(videoService);
    }

    private void startReviewService(int id){
        Intent reviewService = new Intent(this, ReviewService.class);
        reviewService.putExtra("ID", id);
        startService(reviewService);
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
            case LOADER_REVIEW:
                uri = Uri.parse("content://com.rubahapi.review/review");
                return new CursorLoader(MovieDetail.this,
                        uri,
                        MovieDetail.REVIEW_COLUMNS,
                        "REVIEW.MOVIE_ID = ?",
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
            case  LOADER_REVIEW:
                mReviewAdapter.UpdateResult(cursor);
                mReviewAdapter.notifyDataSetChanged();
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

    @Override
    public void onReviewClick(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
