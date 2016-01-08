package com.lochbridge.peike.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.lochbridge.peike.demo.DetailActivity;
import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.model.Movie;
import com.lochbridge.peike.demo.util.Constants;
import com.lochbridge.peike.demo.views.MovieListAdapter;

public class BaseMovieListFragment extends Fragment {
    private static final String LOG_TAG = "BaseMovieListFragment";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        AbsListView listView = (AbsListView) view;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Log.d(LOG_TAG, position+ " clicked");
                TextView titleView = (TextView) viewClicked.findViewById(R.id.primary_text);
                NetworkImageView posterView = (NetworkImageView) viewClicked.findViewById(R.id.item_avatar);
                TextView imdbRatingView = (TextView) viewClicked.findViewById(R.id.imdb_rating);
                TextView doubanRatingView = (TextView) viewClicked.findViewById(R.id.douban_rating);

                Movie movie = new Movie();
                movie.title = titleView.getText().toString();
                movie.imdbId = (String) titleView.getTag();
                movie.posterUrl = posterView.getImageURL();
                movie.backdropUrl = (String) posterView.getTag();
                movie.imdbRating = imdbRatingView.getText().toString();
                movie.doubanRating = doubanRatingView.getText().toString();

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constants.EXTRA_MOVIE, movie);
                startActivity(intent);
            }
        });
    }
}
