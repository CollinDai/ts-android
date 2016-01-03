package com.lochbridge.peike.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.model.Movie;
import com.lochbridge.peike.demo.network.NetworkManager;
import com.lochbridge.peike.demo.util.Constants;
import com.lochbridge.peike.demo.views.MovieListAdapter;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by PDai on 11/9/2015.
 * <p/>
 * Search result list
 */
public class SearchMovieFragment extends BaseMovieListFragment {
    private static final String LOG_TAG = "SearchMovieFragment";
    private String mTitle;
    private TextView mResultMsg;
    private MovieListAdapter mMovieListAdapter;

    public static SearchMovieFragment newInstance(String title) {
        SearchMovieFragment fragment = new SearchMovieFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);// this is required;
        mTitle = getArguments().getString(Constants.EXTRA_TITLE);
        // TODO add languages to search
        Log.d(LOG_TAG, mTitle);
        if (mTitle != null) {
            searchMovie(mTitle);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        mMovieListAdapter = new MovieListAdapter(getActivity());
        listView.setAdapter(mMovieListAdapter);
        super.onViewCreated(listView, savedInstanceState);
        mResultMsg = (TextView) view.findViewById(R.id.empty_id);
    }

    private void searchMovie(String movieName) {
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("search", movieName);
        testObject.saveInBackground();

        NetworkManager.search(getActivity().getApplication(), movieName, new NetworkManager.Callback<List<Movie>>() {
            @Override
            public void onResponse(List<Movie> movies) {
                if (!movies.isEmpty()) {
                    Log.d(LOG_TAG, movies.get(0).imdbId);
                    mMovieListAdapter.updateList(movies);
                } else {
                    mResultMsg.setText("No result found for " + mTitle);
                    mResultMsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
