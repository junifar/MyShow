package com.rubahapi.myshow;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.rubahapi.myshow.adapter.MovieAdapter;
import com.rubahapi.myshow.data.MovieDBHelper;
import com.rubahapi.myshow.data.MovieProvider;
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

public class LatestMovieActivity extends AppCompatActivity {

//    ArrayList<Contact> contacts;
//    ContactsAdapter contactsAdapter;
    public  static final int MOVIE_LOADER = 100;
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_movie);

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

//        contacts = Contact.createContactsList(20000);

//        ContactsAdapter adapter = new ContactsAdapter(this, contacts);

//        rvContacts.setAdapter(adapter);

//        contactsAdapter = new ContactsAdapter(this, null);

        rvContacts.setLayoutManager(new GridLayoutManager(this,2));
        movieAdapter = new MovieAdapter(null);
        rvContacts.setAdapter(movieAdapter);

//        getLoaderManager().initLoader(MOVIE_LOADER,null,this);

        if(isOnline()){
            new getPopularMoviesTask().execute();
        }else
        {
            Toast.makeText(this,"No Connection Available", Toast.LENGTH_LONG);
        }

//        initDB();
    }

//    private void initDB(){
//
//        ContentValues cv = new ContentValues();
//        cv.put(MovieDBHelper.COLUMN_TITLE,"Finding Dori2");
//        cv.put(MovieDBHelper.COLUMN_DESCRIPTION, "Ikan");
//        cv.put(MovieDBHelper.COLUMN_YEARS, 2005);
//        cv.put(MovieDBHelper.COLUMN_IMAGE_PATH,"https://1.bp.blogspot.com/-TL9x5j53h2k/Vu62cZieVaI/AAAAAAAADdU/efYOrlGvzrEwwyhil3UIaXReYVtkTW1Pg/s1600/Finding_Dory_4.jpg");
//
//        ContentValues cv1 = new ContentValues();
//        cv1.put(MovieDBHelper.COLUMN_TITLE,"Finding Dorii5");
//        cv1.put(MovieDBHelper.COLUMN_DESCRIPTION, "Ikan 2");
//        cv1.put(MovieDBHelper.COLUMN_YEARS, 2005);
//        cv1.put(MovieDBHelper.COLUMN_IMAGE_PATH,"https://1.bp.blogspot.com/-TL9x5j53h2k/Vu62cZieVaI/AAAAAAAADdU/efYOrlGvzrEwwyhil3UIaXReYVtkTW1Pg/s1600/Finding_Dory_4.jpg");
//
//        Uri uri = Uri.parse("content://" + MovieProvider.CONTENT_AUTHORITY + "/movie");
//        Log.i("DEBUG TEST ", MovieProvider.CONTENT_AUTHORITY);
//        getContentResolver().delete(uri,null,null);
//        getContentResolver().insert(uri, cv);
//        getContentResolver().insert(uri, cv1);
//        getContentResolver().notifyChange(uri, null);
//    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    class getPopularMoviesTask extends AsyncTask<Void, Void, PopularMovie>{
        @Override
        protected void onPostExecute(PopularMovie popularMovie) {
            super.onPostExecute(popularMovie);
//            contactsAdapter.updateContacts(popularMovie);
//            contactsAdapter.notifyDataSetChanged();
            movieAdapter.updateResult(popularMovie.getResults());
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
                    cv.put(MovieDBHelper.COLUMN_IMAGE_PATH,"http://image.tmdb.org/t/p/w185/" + result.getPosterPath());
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
//            return null;
        }
    }
}
