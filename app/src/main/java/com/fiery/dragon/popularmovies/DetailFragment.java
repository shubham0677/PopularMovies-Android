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

    private ImageView moviePoster;
    private RecyclerView mRecyclerView;
    private TextView movieTitle,movieReleaseDate,movieRating,movieSynopsis,reviewsTitle;
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
        final Movie movie = (Movie) args.get("movie");

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.myFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = getContext().getContentResolver().query(
                        FavoritesProvider.Favorites.CONTENT_URI,
                        new String[]{FavoritesColumns._ID},
                        FavoritesColumns.MOVIE_ID + " = ?",
                        new String[]{Integer.toString(movie.getId())},
                        null);

                if(cursor.moveToFirst()) {
                    Snackbar.make(view, "This movie is already in Favorites", Snackbar.LENGTH_SHORT).show();

                }
                else {
                    ContentValues cv = new ContentValues();
                    cv.put(FavoritesColumns.MOVIE_ID, movie.getId());
                    cv.put(FavoritesColumns.NAME, movie.getOriginalTitle());
                    cv.put(FavoritesColumns.POSTER, movie.getPosterPath());
                    cv.put(FavoritesColumns.RELEASE_DATE, movie.getReleaseDate());
                    cv.put(FavoritesColumns.RATING, movie.getVoteAverage());
                    cv.put(FavoritesColumns.OVERVIEW, movie.getOverview());
                    getActivity().getContentResolver().insert(FavoritesProvider.Favorites.CONTENT_URI, cv);

                    Snackbar.make(view, "Added to favorites" + movie.getOriginalTitle(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        moviePoster = (ImageView) rootView.findViewById(R.id.movie_poster_image);
        movieTitle = (TextView) rootView.findViewById(R.id.movie_title_text);
        movieReleaseDate = (TextView) rootView.findViewById(R.id.movie_release_date_text);
        movieRating = (TextView) rootView.findViewById(R.id.movie_rating_text);
        movieSynopsis = (TextView) rootView.findViewById(R.id.movie_synopsis_text);
        reviewsTitle = (TextView) rootView.findViewById(R.id.title_reviews);

        if(intent != null && intent.hasExtra("movie")) {

            Log.d(LOG_TAG, movie.getPosterPath());

            Picasso.with(getContext()).load(movie.getPosterPath()).into(moviePoster);
            movieTitle.setText(movie.getOriginalTitle());
            movieReleaseDate.setText(movie.getReleaseDate());
            movieRating.setText(String.format(getString(R.string.format_movie_rating),movie.getVoteAverage()));
            movieSynopsis.setText(movie.getOverview());
        }


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailers_list_view);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(mRecyclerView.getContext())
        );

        getTrailers(movie.getId());
        getReviews(movie.getId());

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

    private void getTrailers(int id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TrailersResponse> call = null;
        /* -- */
        Log.d(LOG_TAG,Integer.toString(id));
        /* -- */
        call = apiService.getTrailers(id, BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                List<Trailer> trailersList = response.body().getResults();
                MovieTrailersAdapter trailersAdapter =
                        new MovieTrailersAdapter(getActivity(), trailersList);
                mRecyclerView.setAdapter(trailersAdapter);
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {

            }
        });
    }

    private void getReviews(int id) {
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
                    mRecyclerView.setAdapter(reviewsAdapter);
                }
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {

            }
        });
    }

}