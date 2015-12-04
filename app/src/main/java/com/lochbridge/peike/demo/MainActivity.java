package com.lochbridge.peike.demo;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.lochbridge.peike.demo.fragment.TabFragment;
import com.parse.ParseAnalytics;

public class MainActivity extends AppCompatActivity {
    private static final String LOGTAG = "MainActivity";
    private MenuItem mSearchButton;
    private SearchView mSearchView;
    private View mOverlay;
    private TabFragment mTabFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        HotSubListFragment fragment = new HotSubListFragment();
        mTabFragment = new TabFragment();
        transaction.replace(R.id.main_frag, mTabFragment);
        transaction.commit();

        mOverlay = findViewById(R.id.overlay);
        mOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOGTAG, "Overlay clicked");
                if (mSearchView != null) {
                    mSearchButton.collapseActionView();
                    mSearchView.clearFocus();
                }
                mOverlay.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem.getActionView() == null) {
            Log.d(LOGTAG, "searchItem is NULL");
        }
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setIconifiedByDefault(false);

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(LOGTAG, "Focus has changed to " + hasFocus);
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, 0);
                    if (mOverlay != null) {
                        Log.d(LOGTAG, "Overlay set to visible");
                        mOverlay.setVisibility(View.VISIBLE);
                    }
                } else {
                    v.clearFocus();
                    if (mOverlay != null) {
                        Log.d(LOGTAG, "Overlay set to gone");
                        mOverlay.setVisibility(View.GONE);
                    }
                }
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mSearchButton != null) {
                    mSearchButton.collapseActionView();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

//         set SearchActivity as the activity to handle and display the result;
        ComponentName searchComponent = new ComponentName(this, SearchActivity.class);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(searchComponent);
        mSearchView.setSearchableInfo(searchableInfo);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                Log.d(LOGTAG, "Search icon clicked.");
                mSearchButton = item;
//                onSearchRequested();
                item.getActionView().requestFocus();
                break;
//            case R.id.action_language:
//                break;
            case R.id.action_refresh:
                mTabFragment.refreshHotMovieList();
                break;
            case R.id.action_settings:
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onDetachedFromWindow() {
//        LruMovieCache.persistCache();
    }
}
