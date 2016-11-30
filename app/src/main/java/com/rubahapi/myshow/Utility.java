package com.rubahapi.myshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by prasetia on 11/29/2016.
 */

public class Utility {
    public static boolean isPopular(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.pref_movie_category_key), context.getString(R.string.pref_popular))
                .equals(context.getString(R.string.pref_popular));
    }

    public static String getCategoryState(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  sharedPreferences.getString(context.getString(R.string.pref_movie_category_key),context.getString(R.string.pref_popular));
    }
}
