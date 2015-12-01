package com.lochbridge.peike.demo;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.lochbridge.peike.demo.fragment.SearchMovieFragment;
import com.lochbridge.peike.demo.util.Constants;
import com.parse.ParseAnalytics;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private static final String LOGTAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        Bundle searchData = intent.getExtras();
        String title = searchData.getString(SearchManager.QUERY);
//        String[] languages = searchData.getStringArray(Constants.EXTRA_LANGUAGES);

        Map<String, String> dimensions = new HashMap<>();
        dimensions.put("search", title);

        ParseAnalytics.trackEventInBackground("read", dimensions);
        doMySearch(title);
    }

    private void doMySearch(String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchMovieFragment fragment = SearchMovieFragment.newInstance(title);
        transaction.replace(R.id.search_frag, fragment);
        transaction.commit();
    }


    public void onMovieItemClicked(View viewClicked) {
        TextView titleView = (TextView) viewClicked.findViewById(R.id.primary_text);
        String title = titleView.getText().toString();
        NetworkImageView posterView = (NetworkImageView) viewClicked.findViewById(R.id.item_avatar);
        String posterUrl = posterView.getImageURL();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_TITLE, title);
        intent.putExtra(Constants.EXTRA_POSTER_URL, posterUrl);
        startActivity(intent);
    }
}
