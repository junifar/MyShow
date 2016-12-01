package com.rubahapi.myshow.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;

import com.rubahapi.myshow.Utility;
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

/**
 * Created by prasetia on 11/24/2016.
 */

public class MovieService extends IntentService {
    public MovieService() {
        super("Movie");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection connection = null;
        try{
//            URL url = new URL(MovieURL.getPopularMovie());
            URL url;
            if(Utility.isPopular(this)){
                url = new URL(MovieURL.getPopularMovie());
            }else{
                url = new URL(MovieURL.getTopRatedMovie());
            }
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();

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
                cv.put(MovieDBHelper.COLUMN_ID, result.getId());
                cv.put(MovieDBHelper.COLUMN_TITLE, result.getTitle());
                cv.put(MovieDBHelper.COLUMN_DESCRIPTION, result.getOverview());
                cv.put(MovieDBHelper.COLUMN_YEARS, result.getReleaseDate());
                cv.put(MovieDBHelper.COLUMN_IMAGE_PATH,result.getPosterPath());
                contentValues[i] = cv;
            }

            Uri uri = Uri.parse("content://" + MovieProvider.CONTENT_AUTHORITY + "/movie");
            getContentResolver().delete(uri,null,null);
            getContentResolver().bulkInsert(
                    uri,
                    contentValues
            );
            getContentResolver().notifyChange(uri, null);
        }catch (Exception e){

        }finally {
            if(null != connection){
                connection.disconnect();
            }
        }
    }
}
