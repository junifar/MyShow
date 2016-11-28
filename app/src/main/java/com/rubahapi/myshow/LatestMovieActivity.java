package com.rubahapi.myshow;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.rubahapi.myshow.adapter.MovieAdapter;
import com.rubahapi.myshow.data.MovieDBHelper;
import com.rubahapi.myshow.data.MovieProvider;
import com.rubahapi.myshow.listener.OnMovieClickListener;
import com.rubahapi.myshow.model.MovieURL;
import com.rubahapi.myshow.pojo.popular.PopularMovie;
import com.rubahapi.myshow.pojo.popular.Result;
import com.rubahapi.myshow.singleton.GsonSingleton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class LatestMovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnMovieClickListener {

    public  static final int MOVIE_LOADER = 100;

    public static final String[] MOVIE_COLUMNS = {
            MovieDBHelper.TABLE_MOVIES_NAME + "." + MovieDBHelper.COLUMN_ID,
            MovieDBHelper.COLUMN_IMAGE_PATH
    };

    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_movie);

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        rvContacts.setLayoutManager(new GridLayoutManager(this,2));
        movieAdapter = new MovieAdapter(null, this);
        rvContacts.setAdapter(movieAdapter);

        getLoaderManager().initLoader(MOVIE_LOADER, null, this);


//        MovieSyncAdapter.SyncStart(this);

        if(isOnline()){
            new getPopularMoviesTask().execute();
        }else
        {
            Toast.makeText(this,"No Connection Available", Toast.LENGTH_LONG);
        }
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
        if (i == MOVIE_LOADER){
            return new CursorLoader(LatestMovieActivity.this,
                    uri,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null);
        }
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
    public void onMovieClick(int id) {
        Log.i("INFO", String.valueOf(id));
        Intent intent = new Intent(LatestMovieActivity.this, MovieDetail.class);
        intent.putExtra(MovieDetail.EXTRA_ID, id);
        this.startActivity(intent);

    }


    class getPopularMoviesTask extends AsyncTask<Void, Void, PopularMovie> {
        @Override
        protected void onPostExecute(PopularMovie popularMovie) {
            super.onPostExecute(popularMovie);
            movieAdapter.notifyDataSetChanged();
        }

        @Override
        protected PopularMovie doInBackground(Void... voids) {
            HttpURLConnection conn = null;
            try{
                URL url = new URL(MovieURL.getPopularMovie());
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                conn.connect();

                int responseCode = conn.getResponseCode();
                if(responseCode != HttpURLConnection.HTTP_OK){
                    return null;
                }

                InputStream inputStream = conn.getInputStream();
                if(null == inputStream){
                    return null;
                }

                StringBuilder stringBuilder = new StringBuilder();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String strLine;

                while ((strLine = bufferedReader.readLine()) != null){
                    stringBuilder.append(strLine);
                }

                PopularMovie popularMovie = GsonSingleton.getGson().fromJson(stringBuilder.toString(), PopularMovie.class);
                List<Result> results = popularMovie.getResults();
                ContentValues[] contentValues = new ContentValues[results.size()];
                for (int i=0; i<results.size(); i++){
                    Result result = results.get(i);
                    ContentValues cv = new ContentValues();
                    cv.put(MovieDBHelper.COLUMN_TITLE, result.getTitle());
                    cv.put(MovieDBHelper.COLUMN_DESCRIPTION, result.getOverview());
                    cv.put(MovieDBHelper.COLUMN_YEARS, result.getReleaseDate());
                    cv.put(MovieDBHelper.COLUMN_IMAGE_PATH, result.getPosterPath());
                    contentValues[i] = cv;
                }

                Uri uri = Uri.parse("content://" + MovieProvider.CONTENT_AUTHORITY + "/movie");
                getContentResolver().delete(uri,null,null);
                getContentResolver().bulkInsert(
                        uri,
                        contentValues
                );
                getContentResolver().notifyChange(uri, null);
                return popularMovie;
            } catch (Exception e){
                return null;
            } finally {
                if (null != conn){
                    conn.disconnect();
                }
            }
        }
    }
}
