package com.fiery.dragon.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiery.dragon.popularmovies.DetailFragment;
import com.fiery.dragon.popularmovies.models.Trailer;

import java.util.List;

/**
 * Created by hp on 11/27/2016.
 */

public class MovieTrailersAdapter  extends RecyclerView.Adapter<MovieTrailersAdapter.ViewHolder> {

    private List<Trailer> mTrailers;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mTrailerSource;

        public final View mView;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextView = (TextView) view.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public MovieTrailersAdapter(Context context, List<Trailer> items) {
        mTrailers = items;
    }

    @Override
    public MovieTrailersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new MovieTrailersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieTrailersAdapter.ViewHolder holder, int position) {
        holder.mTrailerSource = mTrailers.get(position).getSource();
        holder.mTextView.setText("Trailer " + (++position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + holder.mTrailerSource)));
                Snackbar.make(v, holder.mTrailerSource, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }
}