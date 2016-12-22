package com.fiery.dragon.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.fiery.dragon.popularmovies.DetailActivity;
import com.fiery.dragon.popularmovies.R;
import com.fiery.dragon.popularmovies.data.FavoritesColumns;
import com.fiery.dragon.popularmovies.data.FavoritesProvider;
import com.fiery.dragon.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by hp on 11/28/2016.
 */

public class FavoritesCursorAdapter extends CursorAdapter {
    private static final String LOG_TAG = FavoritesCursorAdapter.class.getSimpleName();

    public static class ViewHolder {
        public ImageView imageView;
        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.movie_poster_imageView);
        }
    }

    public FavoritesCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie,
                viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context,final Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(
                FavoritesColumns.POSTER))).into(viewHolder.imageView);

//        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Movie movie = new Movie(
////                        cursor.getInt(cursor.getColumnIndex(FavoritesColumns._ID)),
////                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.NAME)),
////                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.POSTER)),
////                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.OVERVIEW)),
////                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.RELEASE_DATE)),
////                        cursor.getDouble(cursor.getColumnIndex(FavoritesColumns.RATING))
////                        );
////                Intent intent = new Intent(context, DetailActivity.class)
////                        .putExtra("movie",movie);
////                context.startActivity(intent);
//                Snackbar.make(view, cursor.getString(cursor.getColumnIndex(
//                        FavoritesColumns.NAME)), Snackbar.LENGTH_SHORT).show();
//            }
//        });
    }
}
