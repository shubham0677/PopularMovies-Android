package com.fiery.dragon.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiery.dragon.popularmovies.adapters.MoviesAdapter;
import com.fiery.dragon.popularmovies.apiService.ApiClient;
import com.fiery.dragon.popularmovies.apiService.ApiInterface;
import com.fiery.dragon.popularmovies.data.FavoritesProvider;
import com.fiery.dragon.popularmovies.models.Movie;
import com.fiery.dragon.popularmovies.models.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MoviesAdapter moviesAdapter;
    private RecyclerView mRecyclerView;
    private String mSortOrder;

    private static final String MOVIES_LIST_KEY = "movies";
    private static final String SELECTED_KEY = "selected_position";
    private static final String SORT_ORDER = "sort_order";
    private static final String LOG_TAG = "MyLog";
    private static final String FAVORITES = "favorites";
    private static final int CURSOR_LOADER_ID = 0;


    public MoviesFragment() {    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movies_grid_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_number_cols)));

        moviesAdapter = new MoviesAdapter((MoviesAdapter.Callbacks) getActivity(), new ArrayList<Movie>());
        mRecyclerView.setAdapter(moviesAdapter);

        if(savedInstanceState != null && savedInstanceState.containsKey(MOVIES_LIST_KEY)
                && !savedInstanceState.getString(SORT_ORDER).equals(FAVORITES)) {
                mSortOrder = savedInstanceState.getString(SORT_ORDER);
                List<Movie> movies = savedInstanceState.getParcelableArrayList(MOVIES_LIST_KEY);
                moviesAdapter.add(movies);
        }
        else {
            mSortOrder = checkSortOrder(getContext());
            fetchMovies(mSortOrder);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG,"onResume");
        if(!mSortOrder.equals(checkSortOrder(getContext()))) {
            Log.d(LOG_TAG,"onResume fetch");
            mSortOrder = checkSortOrder(getContext());
            fetchMovies(mSortOrder);
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SORT_ORDER,mSortOrder);
        outState.putParcelableArrayList(MOVIES_LIST_KEY, moviesAdapter.getMovies());
        super.onSaveInstanceState(outState);
    }

    private String checkSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_order_key),
                context.getString(R.string.pref_sort_order_popular));

    }

    private void fetchMovies(String sortOrder) {
        if(sortOrder.equals(FAVORITES)) {
            Log.d(LOG_TAG,"showingFromDatabase");
            getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        }
        else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MoviesResponse> call =
                    apiService.getMovies(sortOrder, BuildConfig.THE_MOVIE_DB_API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    ArrayList<Movie> movies = (ArrayList) response.body().getResults();
                    moviesAdapter.add(movies);
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), FavoritesProvider.Favorites.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        moviesAdapter.add(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}

