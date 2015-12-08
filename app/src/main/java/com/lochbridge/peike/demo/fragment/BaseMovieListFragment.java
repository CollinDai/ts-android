package com.lochbridge.peike.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.lochbridge.peike.demo.DetailActivity;
import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.util.Constants;
import com.lochbridge.peike.demo.views.MovieListAdapter;

/**
 * Created by PDai on 11/19/2015.
 */
public class BaseMovieListFragment extends Fragment {
    private static final String LOG_TAG = "BaseMovieListFragment";
    protected MovieListAdapter mMovieListAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onViewCreated");
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        mMovieListAdapter = new MovieListAdapter(getActivity());
        listView.setAdapter(mMovieListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Log.d(LOG_TAG, position+ " clicked");
                TextView titleView = (TextView) viewClicked.findViewById(R.id.primary_text);
                NetworkImageView posterView = (NetworkImageView) viewClicked.findViewById(R.id.item_avatar);

                String title = titleView.getText().toString();
                String imdbId = (String) titleView.getTag();
                String posterUrl = posterView.getImageURL();

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constants.EXTRA_TITLE, title);
                intent.putExtra(Constants.EXTRA_IMDB_ID, imdbId);
                intent.putExtra(Constants.EXTRA_POSTER_URL, posterUrl);
                startActivity(intent);
            }
        });
    }
}
