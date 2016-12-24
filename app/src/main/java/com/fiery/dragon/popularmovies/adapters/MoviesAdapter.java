package com.fiery.dragon.popularmovies.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.fiery.dragon.popularmovies.DetailActivity;
import com.fiery.dragon.popularmovies.R;
import com.fiery.dragon.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 11/8/2016.
 */

public class MoviesAdapter extends ArrayAdapter<Movie> {

    List<Movie> movieList;

    public MoviesAdapter(Activity context, List<Movie> movieList) {
        super(context, 0, movieList);
        this.movieList = movieList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie movie = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_movie, parent, false);
        }

        ImageView moviePoster = (ImageView) convertView.findViewById(R.id.movie_poster_imageView);
        Picasso.with(getContext())
                .load(movie.getPosterUrl())
                .into(moviePoster);

//        moviePoster.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), DetailActivity.class)
//                        .putExtra("movie",movie);
//                getContext().startActivity(intent);
//            }
//        });

        return convertView;
    }

}