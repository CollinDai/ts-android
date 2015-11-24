package com.lochbridge.peike.demo.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.model.Subtitle;
import com.lochbridge.peike.demo.views.SubtitleListAdapter;

import java.util.List;

/**
 * Created by PDai on 11/20/2015.
 */
public class SubListFragment extends ListFragment {
    private SubtitleListAdapter mAdapter;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SubtitleListAdapter(getActivity());
        setListAdapter(mAdapter);
        setEmptyText(getResources().getString(R.string.empty_text_sub));
    }

    public void updateList(List<Subtitle> newList) {
        mAdapter.updateList(newList);
    }
}
