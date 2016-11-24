package com.rubahapi.myshow.model;

import com.rubahapi.myshow.BuildConfig;

/**
 * Created by prasetia on 11/23/2016.
 */

public class MovieURL {
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    public static String getPopularMovie(){
        return  BASE_URL + "popular?api_key=" + BuildConfig.TMDB_API_KEY;
    }
}
