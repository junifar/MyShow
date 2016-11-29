package com.rubahapi.myshow.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.rubahapi.myshow.data.MovieDBHelper;
import com.rubahapi.myshow.model.MovieURL;
import com.rubahapi.myshow.pojo.video.Result;
import com.rubahapi.myshow.pojo.video.Video;
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

public class VideoService extends IntentService {

    private int id;

    public VideoService() {
        super("Video");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection connection = null;
        try{
            id = intent.getIntExtra("ID",0);
            URL url = new URL(MovieURL.getVideoURL(id));
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

            Video video = GsonSingleton.getGson().fromJson(stringBuilder.toString(), Video.class);
            List<Result> results = video.getResults();
            ContentValues[] contentValues = new ContentValues[results.size()];
            for (int i=0; i<results.size(); i++){
                Result result = results.get(i);
                ContentValues cv = new ContentValues();
                cv.put(MovieDBHelper.COLUMN_VIDEO_ID, result.getId());
                cv.put(MovieDBHelper.COLUMN_VIDEO_MOVIE_ID, id);
                cv.put(MovieDBHelper.COLUMN_VIDEO_NAME, result.getName());
                cv.put(MovieDBHelper.COLUMN_VIDEO_KEY, result.getKey());
                contentValues[i] = cv;
            }

            Uri uri = Uri.parse("content://com.rubahapi/video");
//            getContentResolver().delete(uri,null,null);
            getContentResolver().bulkInsert(
                    uri,
                    contentValues
            );
            getContentResolver().notifyChange(uri, null);

        }catch (Exception e){
            Log.i("ERROR", e.getMessage());
        }finally {
            if(null != connection){
                    connection.disconnect();
            }
        }
    }
}
