package com.fiery.dragon.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 11/24/2016.
 */

public class Trailer {
    @SerializedName("name")
    private String name;

    @SerializedName("source")
    private String source;

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return "http://www.youtube.com/watch?v=" + source;
    }
}
