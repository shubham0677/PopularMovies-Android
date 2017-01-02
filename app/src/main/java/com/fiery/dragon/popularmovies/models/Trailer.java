/**
 * Created by Shubham on 11/16/2016.
 */

package com.fiery.dragon.popularmovies.models;

import com.google.gson.annotations.SerializedName;

public class Trailer {
    @SerializedName("name")
    private String mName;

    @SerializedName("source")
    private String mSource;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public String getUrl() {
        return "http://www.youtube.com/watch?v=" + mSource;
    }
}
