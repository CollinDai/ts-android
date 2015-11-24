package com.lochbridge.peike.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.model.Movie;
import com.lochbridge.peike.demo.network.NetworkManager;

import java.util.List;

/**
 * Created by PDai on 10/13/2015.
 */
public class HotMovieFragment extends BaseMovieListFragment {
    private static final String LOG_TAG = "HotMovieFragment";
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // call server to get list
        Log.d(LOG_TAG, "Call server to initiate list");
        getTopTen();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_refreshable, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view;
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                getTopTen();
            }
        });
    }

    private void getTopTen() {
        NetworkManager.topTen(getActivity().getApplication(), new NetworkManager.Callback<List<Movie>>() {
            @Override
            public void onResponse(List<Movie> movies) {
                mMovieListAdapter.updateList(movies);
                if (HotMovieFragment.this.mSwipeRefreshLayout.isRefreshing()) {
                    HotMovieFragment.this.mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}
