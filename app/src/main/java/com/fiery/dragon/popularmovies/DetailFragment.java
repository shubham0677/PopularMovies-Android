/**
 * Created by Shubham on 11/16/2016.
 */

package com.fiery.dragon.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiery.dragon.popularmovies.adapters.MoviesAdapter;
import com.fiery.dragon.popularmovies.adapters.TrailersAdapter;
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

public class DetailFragment extends Fragment {

    private Movie mMovie;
    private Trailer mShareTrailer;

    private ImageView mPoster;
    private RecyclerView mTrailersRecyclerView, mReviewsRecyclerView;
    private TextView mTitle,mReleaseDate,mRating,mSynopsis,mNoReviews;
    private Button mButtonAddToFavorites, mButtonRemoveFromFavorites;
    private ShareActionProvider mShareActionProvider;

    public static final String DETAIL_MOVIE = "detail_movie";

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle args = getArguments();
        if(args != null) {
            mMovie = args.getParcelable(DETAIL_MOVIE);
        }

        mPoster = (ImageView) rootView.findViewById(R.id.movie_poster_image);
        mTitle = (TextView) rootView.findViewById(R.id.movie_title_text);
        mReleaseDate = (TextView) rootView.findViewById(R.id.movie_release_date_text);
        mRating = (TextView) rootView.findViewById(R.id.movie_rating_text);
        mSynopsis = (TextView) rootView.findViewById(R.id.movie_synopsis_text);
        mNoReviews = (TextView) rootView.findViewById(R.id.no_reviews);
        mButtonAddToFavorites = (Button) rootView.findViewById(R.id.button_add_to_favorites);
        mButtonRemoveFromFavorites =
                (Button) rootView.findViewById(R.id.button_remove_from_favorites);

        Picasso.with(getContext()).load(mMovie.getPosterUrl()).into(mPoster);

        mTitle.setText(mMovie.getOriginalTitle());
        mRating.setText(String.format(getString(R.string.format_movie_rating),mMovie.getVoteAverage()));
        mReleaseDate.setText(String.format(getString(R.string.format_release_date),mMovie.getFormattedReleaseDate()));
        mSynopsis.setText(mMovie.getOverview());

        mTrailersRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailers_list_view);
        mReviewsRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviews_list_view);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(layoutManager);
        mTrailersRecyclerView.setNestedScrollingEnabled(false);

        layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(layoutManager);


        updateFavoriteButtons();
        getTrailers(mMovie.getId());
        getReviews(mMovie.getId());

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if(mShareTrailer != null) {
            mShareActionProvider.setShareIntent(createShareTrailerIntent());
        }
    }

    /**
     * Create the intent to share trailer url.
     */
    private Intent createShareTrailerIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,mMovie.getOriginalTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT,mMovie.getOriginalTitle() + " "
                + mShareTrailer.getName() + ": " + mShareTrailer.getUrl());
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.action_settings){
            Intent intent=new Intent(getActivity(),SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Show favorite button (add or remove)
     */
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

    /**
     * Query database to check if this movie is already a favorite or not.
     * @return
     */
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

    /**
     * Add movie to favorites table.
     */
    private void addToFavorites() {
        ContentValues cv = new ContentValues();
                    cv.put(FavoritesColumns.MOVIE_ID, mMovie.getId());
                    cv.put(FavoritesColumns.NAME, mMovie.getOriginalTitle());
                    cv.put(FavoritesColumns.POSTER, mMovie.getPosterPath());
                    cv.put(FavoritesColumns.RELEASE_DATE, mMovie.getReleaseDate());
                    cv.put(FavoritesColumns.RATING, mMovie.getVoteAverage());
                    cv.put(FavoritesColumns.OVERVIEW, mMovie.getOverview());
                    getActivity().getContentResolver().insert(FavoritesProvider.Favorites.CONTENT_URI, cv);
                    updateFavoriteButtons();
                    Snackbar.make(getView(), "Added to favorites", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Remove movie from favorites table.
     */
    private  void removeFromFavorites() {
        getContext().getContentResolver().delete(FavoritesProvider.Favorites.CONTENT_URI,
                FavoritesColumns.MOVIE_ID + " = " + mMovie.getId(), null);
        updateFavoriteButtons();
    }

    /**
     * Get trailers information based on movie id using RetroFit.
     * @param id
     */
    private void getTrailers(int id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TrailersResponse> call = null;
        call = apiService.getTrailers(id, BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                List<Trailer> trailersList = response.body().getResults();
                mShareTrailer = trailersList.get(0);
                TrailersAdapter trailersAdapter =
                        new TrailersAdapter(getActivity(), trailersList);
                mTrailersRecyclerView.setAdapter(trailersAdapter);
                mShareActionProvider.setShareIntent(createShareTrailerIntent());
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {

            }
        });
    }

    /**
     * Get reviews based on movie id using RetroFit.
     * @param id
     */
    private void getReviews(int id) {
       ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ReviewsResponse> call = apiService.getReviews(id, BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                List<Review> reviewsList = response.body().getResults();
                if (reviewsList.size() > 0) {
                    mNoReviews.setVisibility(View.GONE);
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