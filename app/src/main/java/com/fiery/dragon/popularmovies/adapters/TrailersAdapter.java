/**
 * Created by Shubham on 11/27/2016.
 */

package com.fiery.dragon.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fiery.dragon.popularmovies.R;
import com.fiery.dragon.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private List<Trailer> mTrailers;
    public static final String TRAILER_URL_PREFIX = "http://www.youtube.com/watch?v=";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        ImageView mThumbnailView;
        Trailer mTrailer;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbnailView = (ImageView) view.findViewById(R.id.trailer_thumbnail);
        }
    }

    public TrailersAdapter(Context context, List<Trailer> items) {
        mTrailers = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Trailer trailer = mTrailers.get(position);
        final Context context = holder.mView.getContext();

        holder.mTrailer = trailer;

        String thumbnailUrl = "http://img.youtube.com/vi/" + trailer.getSource() + "/0.jpg";
        Picasso.with(context)
                .load(thumbnailUrl)
                .config(Bitmap.Config.RGB_565)
                .into(holder.mThumbnailView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(TRAILER_URL_PREFIX + holder.mTrailer.getSource())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }
}