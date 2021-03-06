package com.lochbridge.peike.demo;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.lochbridge.peike.demo.fragment.LanguageDialogFragment;
import com.lochbridge.peike.demo.fragment.SubListFragment;
import com.lochbridge.peike.demo.fragment.SubtitleDialogFragment;
import com.lochbridge.peike.demo.io.LruMovieCache;
import com.lochbridge.peike.demo.model.Movie;
import com.lochbridge.peike.demo.model.Subtitle;
import com.lochbridge.peike.demo.network.NetworkManager;
import com.lochbridge.peike.demo.util.Constants;
import com.lochbridge.peike.demo.util.LangUtil;
import com.lochbridge.peike.demo.util.StorageUtil;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements LanguageDialogFragment.LanguageDialogListener,
        SubtitleDialogFragment.OnFragmentInteractionListener {

    private static final String LOG_TAG = "DetailActivity";
    private SubListFragment subListFragment;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        mMovie = intent.getParcelableExtra(Constants.EXTRA_MOVIE);
        setupSubListFragment();

        TextView titleView = (TextView) findViewById(R.id.movie_title);
        NetworkImageView posterView = (NetworkImageView) findViewById(R.id.poster);

        titleView.setText(mMovie.title);
        NetworkManager.setPoster(this, posterView, mMovie.backdropUrl);
    }

    private void setupSubListFragment() {
        subListFragment = SubListFragment.newInstance(mMovie.imdbId);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_sub_list, subListFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLanguageClicked(View viewClicked) {
        LanguageDialogFragment lanDialog = new LanguageDialogFragment();
        lanDialog.show(getFragmentManager(), "languageDialog");
    }

    @Override
    public void onItemSelected(List<String> languages) {
        Log.d(LOG_TAG, languages.get(0));
        // submit language and movie imdbid to remote service
        // expect list of subtitle name and links
        languages = LangUtil.newInstance(this).nameToISO639_2(languages);
        subListFragment.setListShownNoAnimation(false);
        NetworkManager.searchSubtitle(this, this.mMovie.imdbId, languages, new NetworkManager.Callback<List<Subtitle>>() {
            @Override
            public void onResponse(List<Subtitle> subtitles) {
                if (subtitles != null && !subtitles.isEmpty()) {
                    Log.d(LOG_TAG, "Subtitle size: " + subtitles.size());
                    subListFragment.updateList(subtitles);
                    LruMovieCache.putSubtitleList(DetailActivity.this.mMovie.imdbId, subtitles);
                }
                DetailActivity.this.subListFragment.setListShownNoAnimation(true);
            }
        });
    }

    @Override
    public void persistMovieToDB() {
        if (StorageUtil.isMovieExist(this, this.mMovie.imdbId)) return;
        StorageUtil.writeMovieToDB(this, mMovie);
    }

    @Override
    public void startPlayer(int subFileId) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(Constants.EXTRA_SUB_ID, subFileId);
        intent.putExtra(Constants.EXTRA_TITLE, mMovie.title);
        startActivity(intent);
    }
}
