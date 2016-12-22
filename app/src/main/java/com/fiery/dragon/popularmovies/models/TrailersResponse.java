package com.fiery.dragon.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hp on 11/24/2016.
 */

public class TrailersResponse {
    @SerializedName("youtube")
    private List<Trailer> results;

    public List<Trailer> getResults() {
        return results;
    }

}
