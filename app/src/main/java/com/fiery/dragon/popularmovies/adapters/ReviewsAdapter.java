package com.fiery.dragon.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiery.dragon.popularmovies.R;
import com.fiery.dragon.popularmovies.models.Review;

import java.util.List;

/**
 * Created by hp on 11/24/2016.
 */

public class ReviewsAdapter
            extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

        private List<Review> mReviews;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView mAuthorTextView;
            public final TextView mReviewTextView;

            public ViewHolder(View view) {
                super(view);
                mAuthorTextView = (TextView) view.findViewById(R.id.author_text_view);
                mReviewTextView = (TextView) view.findViewById(R.id.review_text_view);
            }

        }

        public ReviewsAdapter(Context context, List<Review> items) {
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
            Review review = (Review) mReviews.get(position);

            holder.mAuthorTextView.setText(review.getAuthor());
            holder.mReviewTextView.setText(review.getContent());

        }

        @Override
        public int getItemCount() {
            return mReviews.size();
        }
    }