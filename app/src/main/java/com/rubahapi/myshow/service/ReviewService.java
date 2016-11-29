package com.rubahapi.myshow.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;

import com.rubahapi.myshow.data.MovieDBHelper;
import com.rubahapi.myshow.model.MovieURL;
import com.rubahapi.myshow.pojo.review.Result;
import com.rubahapi.myshow.pojo.review.Review;
import com.rubahapi.myshow.singleton.GsonSingleton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by prasetia on 11/29/2016.
 */

public class ReviewService extends IntentService {
    private int id;

    public ReviewService() {
        super("Movie");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection connection = null;
        try{
            id = intent.getIntExtra("ID",0);
            URL url = new URL(MovieURL.getReviewURL(id));
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

            Review review = GsonSingleton.getGson().fromJson(stringBuilder.toString(), Review.class);

            List<Result> results = review.getResults();
            ContentValues[] contentValues = new ContentValues[results.size()];
            for (int i=0; i<results.size(); i++){
                Result result = results.get(i);
                ContentValues cv = new ContentValues();
                cv.put(MovieDBHelper.COLUMN_REVIEW_ID, result.getId());
                cv.put(MovieDBHelper.COLUMN_REVIEW_AUTHOR, result.getAuthor());
                cv.put(MovieDBHelper.COLUMN_REVIEW_CONTENT, result.getContent());
                cv.put(MovieDBHelper.COLUMN_REVIEW_URL, result.getUrl());
                cv.put(MovieDBHelper.COLUMN_REVIEW_MOVIE_ID, id);
                contentValues[i] = cv;
            }

            Uri uri = Uri.parse("content://com.rubahapi.review/review");
//            getContentResolver().delete(uri,null,null);
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
