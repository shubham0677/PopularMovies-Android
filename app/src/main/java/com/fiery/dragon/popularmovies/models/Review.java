package com.fiery.dragon.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 11/24/2016.
 */

public class Review {
    @SerializedName("id")
    private int id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
