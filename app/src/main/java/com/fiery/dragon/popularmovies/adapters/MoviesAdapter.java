package com.fiery.dragon.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.fiery.dragon.popularmovies.DetailActivity;
import com.fiery.dragon.popularmovies.MainActivity;
import com.fiery.dragon.popularmovies.MoviesFragment;
import com.fiery.dragon.popularmovies.R;
import com.fiery.dragon.popularmovies.data.FavoritesColumns;
import com.fiery.dragon.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 11/8/2016.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private final ArrayList<Movie> mList;
    private final Callbacks mCallbacks;
    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();

    public interface Callbacks {
        void open(Movie movie, int position);
    }

    public MoviesAdapter(Callbacks callbacks, ArrayList<Movie> movieList) {
        mCallbacks = callbacks;
        mList = movieList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie, parent, false);
        final Context context = view.getContext();

        int gridColsNumber = context.getResources()
                .getInteger(R.integer.grid_number_cols);

        view.getLayoutParams().height = (int) (parent.getWidth() / gridColsNumber *
                Movie.POSTER_ASPECT_RATIO);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Movie movie = mList.get(position);
        final Context context = holder.mView.getContext();

        holder.mMovie = movie;

        String posterUrl = movie.getPosterUrl();

        Picasso.with(context)
                .load(posterUrl)
                .config(Bitmap.Config.RGB_565)
                .into(holder.mThumbnailView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.open(movie, holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        ImageView mThumbnailView;
        public Movie mMovie;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbnailView = (ImageView) view.findViewById(R.id.movie_poster_imageView);
        }
    }

    public void add(List<Movie> movies) {
        Log.d(LOG_TAG,"Adding movies from list");
        mList.clear();
        Log.d(LOG_TAG,movies.get(0).getPosterPath());
        mList.addAll(movies);
        notifyDataSetChanged();
    }

    public void add(Cursor cursor) {
        Log.d(LOG_TAG,"Adding movies from cursor");
        mList.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Log.d(LOG_TAG,cursor.getString(cursor.getColumnIndex(FavoritesColumns.POSTER)));
                Movie movie = new Movie(
                        cursor.getInt(cursor.getColumnIndex(FavoritesColumns.MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.NAME)),
                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.POSTER)),
                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.RELEASE_DATE)),
                        cursor.getDouble(cursor.getColumnIndex(FavoritesColumns.RATING))
                );
                mList.add(movie);
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getMovies() {
        return mList;
    }


}