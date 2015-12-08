package com.lochbridge.peike.demo.fragment.player;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lochbridge.peike.demo.model.SRTItem;
import com.lochbridge.peike.demo.util.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class PlayerFragment extends Fragment {
    public static final int SIMPLE_THEME = 0;

    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int theme = getArguments().getInt(Constants.ARG_PLAYER_THEME);
        if (theme == SIMPLE_THEME) {
        }
        return null;
    }


    class ReadFileTask extends AsyncTask<String, SRTItem, Void> {

        @Override
        protected Void doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onProgressUpdate(SRTItem... values) {
            super.onProgressUpdate(values);
        }
    }

}
