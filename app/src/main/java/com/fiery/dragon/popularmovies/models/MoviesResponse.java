/**
 * Created by Shubham on 11/16/2016.
 */

package com.fiery.dragon.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {
    @SerializedName("page")
    private int mPage;

    @SerializedName("results")
    private List<Movie> mResults;

    @SerializedName("total_results")
    private int mTotalResults;

    @SerializedName("total_pages")
    private int mTotalPages;

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        this.mPage = page;
    }

    public List<Movie> getResults() {
        return mResults;
    }

    public void setResults(List<Movie> mResults) {
        this.mResults = mResults;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(int totalResults) {
        this.mTotalResults = totalResults;
    }

    public int getTotalPages() {
        return mTotalPages;
    }
}