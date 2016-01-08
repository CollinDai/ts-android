package com.lochbridge.peike.demo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lochbridge.peike.demo.R;

import java.util.TimerTask;

public class TimerFragment extends Fragment {
    TextView minAndSecView;
    long startTime;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new TimerRunnable();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        minAndSecView = (TextView) view.findViewById(R.id.timer_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    class TimerRunnable implements Runnable {

        @Override
        public void run() {
            long millis = getPlayerCurrentTime() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            minAndSecView.setText(String.format("%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }

        long getPlayerCurrentTime() {
            return System.currentTimeMillis();
        }
    }
}


