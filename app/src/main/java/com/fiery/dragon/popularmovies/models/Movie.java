/**
 * Created by Shubham on 11/16/2016.
 */

package com.fiery.dragon.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fiery.dragon.popularmovies.R;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Movie implements Parcelable {
    @SerializedName("id")
    private int mId;
    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("vote_average")
    private Double mVoteAverage;

    public static final float POSTER_ASPECT_RATIO = 1.5f;
    public static final String LOG_TAG = Movie.class.getSimpleName();

    public Movie(int id, String originalTitle, String posterPath, String overview,
                 String releaseDate, Double voteAverage) {
        this.mId = id;
        this.mOriginalTitle = originalTitle;
        this.mPosterPath = posterPath;
        this.mOverview = overview;
        this.mReleaseDate = releaseDate;
        this.mVoteAverage = voteAverage;
    }

    public Movie(Parcel parcel) {
        mId = parcel.readInt();
        mOriginalTitle = parcel.readString();
        mPosterPath = parcel.readString();
        mOverview = parcel.readString();
        mReleaseDate = parcel.readString();
        mVoteAverage = parcel.readDouble();
    }

    public Integer getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getPosterUrl() {
        return "http://image.tmdb.org/t/p/w500/" + mPosterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getFormattedReleaseDate() {
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        try {
            Date date = inputFormat.parse(mReleaseDate);
            return DateFormat.getDateInstance().format(date);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "The Release date was not parsed successfully: " + mReleaseDate);
        }
        return mReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeDouble(mVoteAverage);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}