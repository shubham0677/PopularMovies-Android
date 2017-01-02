/**
 * Created by Shubham on 11/26/2016.
 */

package com.fiery.dragon.popularmovies.apiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static Retrofit sRetrofit = null;

    public static Retrofit getClient() {
        if (sRetrofit==null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }
}
