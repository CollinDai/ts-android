package com.lochbridge.peike.demo.fragment;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.io.LruMovieCache;
import com.lochbridge.peike.demo.model.Subtitle;
import com.lochbridge.peike.demo.util.Constants;
import com.lochbridge.peike.demo.views.SubtitleListAdapter;

import java.util.List;

/**
 * Created by PDai on 11/20/2015.
 * Extends ListFragment instead of Fragment to utilize it's pre-defined
 * loading progress bar and empty item text.
 */
public class SubListFragment extends ListFragment {
    private static final String LOG_TAG = "SubListFragment";
    private SubtitleListAdapter mAdapter;

    public static SubListFragment newInstance(String imdbId) {

        Bundle args = new Bundle();
        args.putString(Constants.ARG_IMDB_ID, imdbId);
        SubListFragment fragment = new SubListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SubtitleListAdapter(getActivity());
        setListAdapter(mAdapter);
        setEmptyText(getResources().getString(R.string.empty_text_sub));

        String imdbId = getArguments().getString(Constants.ARG_IMDB_ID);
        Log.d(LOG_TAG, "IMDB ID: " + imdbId);
        if (imdbId != null) {
            List<Subtitle> subtitles = LruMovieCache.getSubtitleList(imdbId);
            if (subtitles != null && !subtitles.isEmpty()) {
                Log.d(LOG_TAG, "Subtitle size: " + subtitles.size());
                updateList(subtitles);
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // show dialog
        Log.d(LOG_TAG, "Subtitle clicked");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);

        Subtitle subtitle = (Subtitle) l.getItemAtPosition(position);
        SubtitleDialogFragment dialog = new SubtitleDialogFragment();
        dialog.setArg((ImageView) v.findViewById(R.id.download_icon), subtitle);
        dialog.show(transaction, "dialog");
    }

    public void updateList(List<Subtitle> newList) {
        mAdapter.updateList(newList);
    }
}
