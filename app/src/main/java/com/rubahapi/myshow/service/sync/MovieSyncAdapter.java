package com.rubahapi.myshow.service.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

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

public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {

        HttpURLConnection connection = null;
        try{
            URL url = new URL(MovieURL.getPopularMovie());
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
                cv.put(MovieDBHelper.COLUMN_TITLE, result.getTitle());
                cv.put(MovieDBHelper.COLUMN_DESCRIPTION, result.getOverview());
                cv.put(MovieDBHelper.COLUMN_YEARS, result.getReleaseDate());
                cv.put(MovieDBHelper.COLUMN_IMAGE_PATH,"http://image.tmdb.org/t/p/w185/" + result.getPosterPath());
                contentValues[i] = cv;
            }

            Uri uri = Uri.parse("content://" + MovieProvider.CONTENT_AUTHORITY + "/movie");
            getContext().getContentResolver().delete(uri,null,null);
            getContext().getContentResolver().bulkInsert(
                    uri,
                    contentValues
            );
            getContext().getContentResolver().notifyChange(uri, null);
        }catch (Exception e){

        }finally {
            if(null != connection){
                connection.disconnect();
            }
        }
    }

    public static void SyncStart(Context context){
        AccountManager accountManager = (AccountManager) context.getSystemService(context.ACCOUNT_SERVICE);
        Account account = new Account("juni",MovieProvider.CONTENT_AUTHORITY);

        String password = accountManager.getPassword(account);
        if(null == password){
            boolean success = accountManager.addAccountExplicitly(account,"",null);
            if(success){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    SyncRequest request = new SyncRequest.Builder().
                            syncPeriodic(60 * 180, (60 * 180) / 3).
                            setSyncAdapter(account, MovieProvider.CONTENT_AUTHORITY).
                            setExtras(new Bundle()).build();
                    ContentResolver.requestSync(request);
                } else {
                    ContentResolver.addPeriodicSync(account,
                            MovieProvider.CONTENT_AUTHORITY, new Bundle(), 60 * 180);
                }

                ContentResolver.setSyncAutomatically(account, MovieProvider.CONTENT_AUTHORITY, true);

                Bundle bundle = new Bundle();
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                ContentResolver.requestSync(account, MovieProvider.CONTENT_AUTHORITY, bundle);
            }
        }
    }
}
