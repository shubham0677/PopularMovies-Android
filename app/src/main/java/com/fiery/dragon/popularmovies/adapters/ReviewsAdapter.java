/**
 * Created by Shubham on 11/24/2016.
 */

package com.fiery.dragon.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiery.dragon.popularmovies.R;
import com.fiery.dragon.popularmovies.models.Review;

import java.util.List;

public class ReviewsAdapter
            extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

        private List<Review> mReviews;
        final Context mContext;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            TextView mAuthorTextView;
            TextView mContentTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mAuthorTextView = (TextView) view.findViewById(R.id.author_text_view);
                mContentTextView = (TextView) view.findViewById(R.id.content_text_view);
            }

        }

        public ReviewsAdapter(Context context, List<Review> items) {
            this.mContext = context;
            mReviews = items;
        }

        @Override
        public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_review, parent, false);
            return new ReviewsAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ReviewsAdapter.ViewHolder holder, int position) {
            final Review review = mReviews.get(position);

            holder.mAuthorTextView.setText(review.getAuthor());
            holder.mContentTextView.setText(review.getContent());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl())));
                }
            });

        }

        @Override
        public int getItemCount() {
            return mReviews.size();
        }
    }