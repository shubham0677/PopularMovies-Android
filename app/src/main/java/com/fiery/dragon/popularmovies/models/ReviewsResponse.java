/**
 * Created by Shubham on 11/16/2016.
 */

package com.fiery.dragon.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse {
    @SerializedName("results")
    private List<Review> mResults;

    public List<Review> getResults() {
        return mResults;
    }

}
