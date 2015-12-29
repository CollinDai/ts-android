package com.lochbridge.peike.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.io.LruMovieCache;
import com.lochbridge.peike.demo.model.Movie;
import com.lochbridge.peike.demo.network.NetworkManager;

import java.util.List;

/**
 * Created by PDai on 10/13/2015.
 */
public class HotMovieFragment extends BaseMovieListFragment {
    private static final String LOG_TAG = "HotMovieFragment";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmptyText;
    private View mLoadingIndicator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_refreshable, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mEmptyText = (TextView) view.findViewById(R.id.empty_id);
        mLoadingIndicator = view.findViewById(R.id.progressContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                getTopTen(true);
            }
        });
        getTopTen(false);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG_TAG, "onAttach()");
        super.onAttach(context);
    }

    public void getTopTen(boolean pulled) {
        if (!pulled) {
            // App initiative loading
            Log.d(LOG_TAG, mLoadingIndicator == null ? "NULL" : "NOT NULL");
            if (mLoadingIndicator != null) {
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }
        }
        NetworkManager.topTen(getActivity().getApplication(), new NetworkManager.Callback<List<Movie>>() {
            @Override
            public void onResponse(List<Movie> movies) {
                if (movies != null && !movies.isEmpty()) {
                    mMovieListAdapter.updateList(movies);
                    LruMovieCache.putMovieList(movies);
                } else {
                    mEmptyText.setText("No movie found.");
                }
                if (HotMovieFragment.this.mSwipeRefreshLayout.isRefreshing()) {
                    HotMovieFragment.this.mSwipeRefreshLayout.setRefreshing(false);
                }
                if (mLoadingIndicator != null)
                    mLoadingIndicator.setVisibility(View.GONE);
            }
        });
    }
}
