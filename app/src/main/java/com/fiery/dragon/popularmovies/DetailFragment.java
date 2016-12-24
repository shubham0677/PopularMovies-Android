package com.fiery.dragon.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiery.dragon.popularmovies.adapters.MovieTrailersAdapter;
import com.fiery.dragon.popularmovies.adapters.ReviewsAdapter;
import com.fiery.dragon.popularmovies.apiService.ApiClient;
import com.fiery.dragon.popularmovies.apiService.ApiInterface;
import com.fiery.dragon.popularmovies.data.FavoritesColumns;
import com.fiery.dragon.popularmovies.data.FavoritesProvider;
import com.fiery.dragon.popularmovies.models.Movie;
import com.fiery.dragon.popularmovies.models.Review;
import com.fiery.dragon.popularmovies.models.ReviewsResponse;
import com.fiery.dragon.popularmovies.models.Trailer;
import com.fiery.dragon.popularmovies.models.TrailersResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shubham on 11/16/2016.
 */

public class DetailFragment extends Fragment {

    private Movie mMovie;

    private ImageView moviePoster;
    private RecyclerView mTrailersRecyclerView, mReviewsRecyclerView;
    private TextView movieTitle,movieReleaseDate,movieRating,movieSynopsis,reviewsTitle;
    private Button mButtonAddToFavorites, mButtonRemoveFromFavorites;
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        Bundle args = intent.getExtras();
        mMovie = (Movie) args.get("movie");

        moviePoster = (ImageView) rootView.findViewById(R.id.movie_poster_image);
        movieTitle = (TextView) rootView.findViewById(R.id.movie_title_text);
        movieReleaseDate = (TextView) rootView.findViewById(R.id.movie_release_date_text);
        movieRating = (TextView) rootView.findViewById(R.id.movie_rating_text);
        movieSynopsis = (TextView) rootView.findViewById(R.id.movie_synopsis_text);
        reviewsTitle = (TextView) rootView.findViewById(R.id.title_reviews);
        mButtonAddToFavorites = (Button) rootView.findViewById(R.id.button_add_to_favorites);
        mButtonRemoveFromFavorites =
                (Button) rootView.findViewById(R.id.button_remove_from_favorites);

        if(intent != null && intent.hasExtra("favorite")) {
            Picasso.with(getContext()).load(mMovie.getPosterPath()).into(moviePoster);
        }
        else {
            Picasso.with(getContext()).load(mMovie.getPosterUrl()).into(moviePoster);
        }

        updateFavoriteButtons();

        movieTitle.setText(mMovie.getOriginalTitle());
        movieReleaseDate.setText(mMovie.getReleaseDate());
        movieRating.setText(String.format(getString(R.string.format_movie_rating),mMovie.getVoteAverage()));
        movieSynopsis.setText(mMovie.getOverview());

        mTrailersRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailers_list_view);
        mReviewsRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviews_list_view);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(layoutManager);
        mTrailersRecyclerView.setNestedScrollingEnabled(false);

        layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(layoutManager);

        getTrailers(mMovie.getId());
        getReviews(mMovie.getId());

        return rootView;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if(id==R.id.action_settings){
//            Intent intent=new Intent(getActivity(),SettingsActivity.class);
//            startActivity(intent);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void updateFavoriteButtons() {
        if (isFavorite()) {
            mButtonRemoveFromFavorites.setVisibility(View.VISIBLE);
            mButtonAddToFavorites.setVisibility(View.GONE);
        } else {
            mButtonAddToFavorites.setVisibility(View.VISIBLE);
            mButtonRemoveFromFavorites.setVisibility(View.GONE);
        }
        mButtonAddToFavorites.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToFavorites();
                    }
                });

        mButtonRemoveFromFavorites.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFromFavorites();
                    }
                });
    }

    private boolean isFavorite() {
        Cursor cursor = getContext().getContentResolver().query(
                FavoritesProvider.Favorites.CONTENT_URI,
                new String[]{FavoritesColumns._ID},
                FavoritesColumns.MOVIE_ID + " = ?",
                new String[]{Integer.toString(mMovie.getId())},
                null);
        if(cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    private void addToFavorites() {
        ContentValues cv = new ContentValues();
                    cv.put(FavoritesColumns.MOVIE_ID, mMovie.getId());
                    cv.put(FavoritesColumns.NAME, mMovie.getOriginalTitle());
                    cv.put(FavoritesColumns.POSTER, mMovie.getPosterUrl());
                    cv.put(FavoritesColumns.RELEASE_DATE, mMovie.getReleaseDate());
                    cv.put(FavoritesColumns.RATING, mMovie.getVoteAverage());
                    cv.put(FavoritesColumns.OVERVIEW, mMovie.getOverview());
                    getActivity().getContentResolver().insert(FavoritesProvider.Favorites.CONTENT_URI, cv);
                    updateFavoriteButtons();
                    Snackbar.make(getView(), "Added to favorites" + mMovie.getOriginalTitle(), Snackbar.LENGTH_SHORT).show();
    }

    private  void removeFromFavorites() {
        getContext().getContentResolver().delete(FavoritesProvider.Favorites.CONTENT_URI,
                FavoritesColumns.MOVIE_ID + " = " + mMovie.getId(), null);
        updateFavoriteButtons();
    }

    private void getTrailers(int id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TrailersResponse> call = null;
        call = apiService.getTrailers(id, BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                List<Trailer> trailersList = response.body().getResults();
                MovieTrailersAdapter trailersAdapter =
                        new MovieTrailersAdapter(getActivity(), trailersList);
                mTrailersRecyclerView.setAdapter(trailersAdapter);
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {

            }
        });
    }

    private void getReviews(int id) {

        Log.d(LOG_TAG,Integer.toString(id));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ReviewsResponse> call = apiService.getReviews(id, BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                List<Review> reviewsList = response.body().getResults();
                if (reviewsList.size() > 0) {
                    reviewsTitle.setText("Reviews");
                    ReviewsAdapter reviewsAdapter =
                            new ReviewsAdapter(getActivity(), reviewsList);
                    mReviewsRecyclerView.setAdapter(reviewsAdapter);
                }
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {

            }
        });
    }

}