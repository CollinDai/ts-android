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
import com.lochbridge.peike.demo.model.Subtitle;
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SubtitleListAdapter(getActivity());
        setListAdapter(mAdapter);
        setEmptyText(getResources().getString(R.string.empty_text_sub));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // show dialog
        Log.d(LOG_TAG, "Subtitle clicked");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);

        Subtitle subtitle = (Subtitle) l.getItemAtPosition(position);
        SubtitleDialogFragment dialog = new SubtitleDialogFragment();
        dialog.setSubtitle(subtitle);
        dialog.show(transaction, "dialog");
    }

    public void updateList(List<Subtitle> newList) {
        mAdapter.updateList(newList);
    }
}
