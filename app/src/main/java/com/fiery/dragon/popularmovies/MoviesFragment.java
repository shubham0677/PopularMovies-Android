package com.fiery.dragon.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fiery.dragon.popularmovies.adapters.FavoritesCursorAdapter;
import com.fiery.dragon.popularmovies.adapters.MoviesAdapter;
import com.fiery.dragon.popularmovies.apiService.ApiClient;
import com.fiery.dragon.popularmovies.apiService.ApiInterface;
import com.fiery.dragon.popularmovies.data.FavoritesColumns;
import com.fiery.dragon.popularmovies.data.FavoritesProvider;
import com.fiery.dragon.popularmovies.models.Movie;
import com.fiery.dragon.popularmovies.models.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private MoviesAdapter moviesAdapter;
    private FavoritesCursorAdapter mAdapter;
    private GridView gridView;
    private List<Movie> moviesList;
    private String mSortOrder;
    private int mPosition = GridView.INVALID_POSITION;
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
        gridView = (GridView) rootView.findViewById(R.id.moviesGridView);
        mAdapter = new FavoritesCursorAdapter(getActivity(), null);

        if(savedInstanceState != null && savedInstanceState.containsKey(MOVIES_LIST_KEY)
                && !savedInstanceState.getString(SORT_ORDER).equals(FAVORITES)) {
                mSortOrder = savedInstanceState.getString(SORT_ORDER);
                moviesList = (List) savedInstanceState.getParcelableArrayList(MOVIES_LIST_KEY);
                moviesAdapter = new MoviesAdapter(getActivity(), moviesList);
                gridView.setAdapter(moviesAdapter);
            
        }
        else {
            mSortOrder = checkSortOrder(getContext());
            showMovies(mSortOrder);
        }

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Movie movieClicked = moviesAdapter.getItem(i);
//                Intent intent = new Intent(getActivity(), DetailActivity.class)
//                        .putExtra("movie",movieClicked);
//                startActivity(intent);
//            }
//        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        Log.d(LOG_TAG,"onResume");
        if(!mSortOrder.equals(checkSortOrder(getContext()))) {
            Log.d(LOG_TAG,"onResume fetch");
            mSortOrder = checkSortOrder(getContext());
            showMovies(mSortOrder);
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SORT_ORDER,mSortOrder);
        outState.putParcelableArrayList(MOVIES_LIST_KEY, (ArrayList) moviesList);
        super.onSaveInstanceState(outState);
    }

    private String checkSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_order_key),
                context.getString(R.string.pref_sort_order_popular));

    }

    private void showMovies(String sortOrder) {
        if(sortOrder.equals(FAVORITES)) {
            Log.d(LOG_TAG,"showingFromDatabase");
            showMoviesFromDatabase();
        }
        else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MoviesResponse> call = null;
            if (sortOrder.equals("popularity.desc")) {
                call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_KEY);
            } else if (sortOrder.equals("vote_count.desc")) {
                call = apiService.getHighestRatedMovies(BuildConfig.THE_MOVIE_DB_API_KEY);
            }
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    moviesList = response.body().getResults();
                    moviesAdapter = new MoviesAdapter(getActivity(), moviesList);
                    gridView.setAdapter(moviesAdapter);
                    gridView.setOnItemClickListener(myOnItemClickListener2);
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {

                }
            });
        }
    }

    private void showMoviesFromDatabase() {
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(myOnItemClickListener);
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
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Cursor cursor = (Cursor)adapterView.getItemAtPosition(i);
            Movie movie = new Movie(
                        cursor.getInt(cursor.getColumnIndex(FavoritesColumns.MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.NAME)),
                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.POSTER)),
                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(FavoritesColumns.RELEASE_DATE)),
                        cursor.getDouble(cursor.getColumnIndex(FavoritesColumns.RATING))
                        );
                Intent intent = new Intent(getContext(), DetailActivity.class)
                        .putExtra("movie",movie);
                startActivity(intent);
        }
    };

    AdapterView.OnItemClickListener myOnItemClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Movie movie = (Movie) adapterView.getItemAtPosition(i);
            Intent intent = new Intent(getContext(), DetailActivity.class)
                        .putExtra("movie",movie);
                startActivity(intent);

        }
    };

}

