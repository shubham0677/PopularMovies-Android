/**
 * Created by Shubham on 11/16/2016.
 */

package com.fiery.dragon.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResponse {
    @SerializedName("youtube")
    private List<Trailer> mResults;

    public List<Trailer> getResults() {
        return mResults;
    }

}
