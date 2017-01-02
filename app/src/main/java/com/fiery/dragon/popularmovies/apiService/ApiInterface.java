/**
 * Created by Shubham on 11/14/2016.
 */

package com.fiery.dragon.popularmovies.apiService;

import com.fiery.dragon.popularmovies.models.MoviesResponse;
import com.fiery.dragon.popularmovies.models.TrailersResponse;
import com.fiery.dragon.popularmovies.models.ReviewsResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("{sort_by}")
    Call<MoviesResponse> getMovies(@Path("sort_by") String sort_by, @Query("api_key") String apiKey);

    @GET("{id}/trailers")
    Call<TrailersResponse> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") int id, @Query("api_key") String apiKey);

}