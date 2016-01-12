package com.lochbridge.peike.demo.fragment.player;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lochbridge.peike.demo.model.SRTItem;
import com.lochbridge.peike.demo.util.Constants;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class PlayerFragment extends Fragment {
    public static final int SIMPLE_THEME = 0;
    protected List<SRTItem> mSubContent;
    public PlayerFragment() {
        // Required empty public constructor
    }

    class ReadFileTask extends AsyncTask<String, SRTItem, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            SRTItem srtItem = new SRTItem();

            // TODO read sub content from file
            // and then construct SRTItem from it.

            publishProgress(srtItem);
            return true;
        }

        @Override
        protected void onProgressUpdate(SRTItem... values) {
            mSubContent.add(values[0]);
        }
    }

    public abstract void nextTapped();
    public abstract void previousTapped();

}
