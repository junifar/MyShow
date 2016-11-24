package com.rubahapi.myshow.service.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by prasetia on 11/24/2016.
 */

public class MovieSyncService extends Service {

    private static  final Object sSyncAdapterLock = new Object();
    private static MovieSyncAdapter movieSyncAdapter = null;

    public MovieSyncService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sSyncAdapterLock){
            if(movieSyncAdapter == null){
                movieSyncAdapter = new MovieSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return  movieSyncAdapter.getSyncAdapterBinder();
    }
}
