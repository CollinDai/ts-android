package com.lochbridge.peike.demo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lochbridge.peike.demo.PlayerActivity;
import com.lochbridge.peike.demo.R;

/**
 * Created by Peike on 1/10/2016.
 */
public class PlayerControlFragment extends Fragment {

    private static final String LOG_TAG = "PlayerControlFragment";

    public interface PlayerControlListener {
        void nextTapped();
        void previousTapped();
    }

    private Button nextSubButton;
    private Button previousButton;
    private PlayerControlListener controlListener;

    @Override
    public void onAttach(Context context) {
        Log.d(LOG_TAG, "onAttach()");
        super.onAttach(context);
        try {
            controlListener = (PlayerControlListener) context;
            Log.d(LOG_TAG, "Listener assigned successfully!");
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement PlayerControlListener");
        }
    }

    /**
     * For before API 23
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "onAttach(Activity)");
        try {
            controlListener = (PlayerControlListener) activity;
            Log.d(LOG_TAG, "Listener assigned successfully!");
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PlayerControlListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_control,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nextSubButton = (Button) view.findViewById(R.id.next_sub_button);
        previousButton = (Button) view.findViewById(R.id.previous_sub_button);
        nextSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerControlFragment.this.controlListener.nextTapped();
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerControlFragment.this.controlListener.previousTapped();
            }
        });
    }
}

