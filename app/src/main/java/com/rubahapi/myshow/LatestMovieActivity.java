package com.rubahapi.myshow;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rubahapi.myshow.adapter.MovieAdapter;
import com.rubahapi.myshow.data.MovieDBHelper;
import com.rubahapi.myshow.data.MovieProvider;
import com.rubahapi.myshow.listener.OnMovieClickListener;
import com.rubahapi.myshow.service.MovieService;

public class LatestMovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnMovieClickListener {

    public static final int MOVIE_LOADER = 100;
    public static final int FAVOURITE_LOADER = 200;
    private boolean isPopular;
    private String currentState;

    public static final String[] MOVIE_COLUMNS = {
            MovieDBHelper.TABLE_MOVIES_NAME + "." + MovieDBHelper.COLUMN_ID,
            MovieDBHelper.COLUMN_IMAGE_PATH,
            MovieDBHelper.COLUMN_TITLE,
            MovieDBHelper.COLUMN_YEARS,
            MovieDBHelper.COLUMN_DURATION,
            MovieDBHelper.COLUMN_RATING,
            MovieDBHelper.COLUMN_DESCRIPTION
    };

    public static final String[] FAVOURITE_COLUMNS = {
            MovieDBHelper.TABLE_FAVOURITE_NAME + "." + MovieDBHelper.COLUMN_ID,
            MovieDBHelper.COLUMN_IMAGE_PATH,
            MovieDBHelper.COLUMN_TITLE,
            MovieDBHelper.COLUMN_YEARS,
            MovieDBHelper.COLUMN_DURATION,
            MovieDBHelper.COLUMN_RATING,
            MovieDBHelper.COLUMN_DESCRIPTION
    };

    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_movie);

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        //check default URL MOvie is popular or top rated
        this.isPopular = Utility.isPopular(this);
        this.currentState = Utility.getCategoryState(this);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvContacts.setLayoutManager(new GridLayoutManager(this,4));
        }else{
            rvContacts.setLayoutManager(new GridLayoutManager(this,2));
        }
        movieAdapter = new MovieAdapter(null, this, this);
        rvContacts.setAdapter(movieAdapter);

        String prefFavourite = getResources().getString(R.string.pref_favourite);
//        String currentState =  Utility.getCategoryState(this);
        if(Utility.getCategoryState(this).equals(prefFavourite)){
            getLoaderManager().initLoader(FAVOURITE_LOADER, null, this);
        }else {
            getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        }


//        MovieSyncAdapter.SyncStart(this);

        if(isOnline()){
//            new getPopularMoviesTask().execute();
            startMovieService();
        }else
        {
            Toast.makeText(this,"No Connection Available", Toast.LENGTH_LONG);
        }

    }

    private boolean isMovieCategoryChange(){
        if(isPopular != Utility.isPopular(this)){
            isPopular = Utility.isPopular(this);
            currentState = Utility.getCategoryState(this);
            return true;
        }
        return false;
    }

    private void startMovieService(){
        Intent movieService = new Intent(this, MovieService.class);
        startService(movieService);
    }

    public Context getContext(){
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri = Uri.parse("content://" + MovieProvider.CONTENT_AUTHORITY + "/movie");
        Uri uriFavourite = Uri.parse("content://com.rubahapi.favourite/favourite");
        switch (i){
            case MOVIE_LOADER:
                return new CursorLoader(LatestMovieActivity.this,
                        uri,
                        MOVIE_COLUMNS,
                        null,
                        null,
                        null);
            case FAVOURITE_LOADER:
                return new CursorLoader(LatestMovieActivity.this,
                        uriFavourite,
                        FAVOURITE_COLUMNS,
                        null,
                        null,
                        null);
        }

//        if (i == MOVIE_LOADER){
//        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        movieAdapter.updateResult(cursor);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.updateResult(null);
    }

    @Override
    public void onMovieClick(int id, int movie_id) {
        Log.i("INFO", String.valueOf(id));
        Intent intent = new Intent(LatestMovieActivity.this, MovieDetail.class);
        intent.putExtra(MovieDetail.EXTRA_ID, id);
        intent.putExtra(MovieDetail.EXTRA_MOVIE_ID, movie_id);
        this.startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String prefFavourite = getResources().getString(R.string.pref_favourite);
//        String currentState =  Utility.getCategoryState(this);
        if(Utility.getCategoryState(this).equals(prefFavourite) && !Utility.getCategoryState(this).equals(currentState)) {
            setFavouriteMovieAdapterSource();
        }else{
            if (isMovieCategoryChange()) {
                startMovieService();
                setOtherMovieAdapterResource();
            }
        }

    }

    private void setOtherMovieAdapterResource(){
        Uri uri = Uri.parse("content://" + MovieProvider.CONTENT_AUTHORITY + "/movie");

        Cursor cursor = getContentResolver().query(uri,
                MOVIE_COLUMNS,
                null,
                null,
                null);
        movieAdapter.updateResult(cursor);
        movieAdapter.notifyDataSetChanged();
    }

    private void setFavouriteMovieAdapterSource() {
        Uri uriFavourite = Uri.parse("content://com.rubahapi.favourite/favourite");

        Cursor cursor = getContentResolver().query(uriFavourite,
                FAVOURITE_COLUMNS,
                null,
                null,
                null);
        movieAdapter.updateResult(cursor);
        movieAdapter.notifyDataSetChanged();
    }
}
