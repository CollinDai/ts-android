package com.lochbridge.peike.demo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void startTimer() {
        startTime = SystemClock.uptimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void setTimer(int millisecond) {
        startTime = SystemClock.uptimeMillis() - millisecond;
    }

    class TimerRunnable implements Runnable {

        @Override
        public void run() {
            long millis = SystemClock.uptimeMillis() - startTime;
            minAndSecView.setText(millisToTime(millis));
            timerHandler.postDelayed(this, 500);
        }

        private String millisToTime(long millisecond) {
            int seconds = (int) (millisecond / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            return String.format("%d:%02d", minutes, seconds);
        }
    }
}


