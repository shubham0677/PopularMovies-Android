package com.fiery.dragon.popularmovies.apiService;

import com.fiery.dragon.popularmovies.models.MoviesResponse;
import com.fiery.dragon.popularmovies.models.TrailersResponse;
import com.fiery.dragon.popularmovies.models.ReviewsResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hp on 11/14/2016.
 */

public interface ApiInterface {
    @GET("discover/movie?sort_by=vote_count.desc")
    Call<MoviesResponse> getHighestRatedMovies(@Query("api_key") String apiKey);

    @GET("discover/movie?sort_by=popularity.desc")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/trailers")
    Call<TrailersResponse> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") int id, @Query("api_key") String apiKey);

}