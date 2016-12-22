package com.fiery.dragon.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hp on 11/24/2016.
 */

public class ReviewsResponse {
    @SerializedName("results")
    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }
}
