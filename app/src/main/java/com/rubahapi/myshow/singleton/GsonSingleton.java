package com.rubahapi.myshow.singleton;

import com.google.gson.Gson;

/**
 * Created by hendrysetiadi on 12/11/2016.
 */

public class GsonSingleton {
    static Gson gson;
    public static Gson getGson(){
        if (null == gson) {
            return new Gson();
        }
        return gson;
    }
}
