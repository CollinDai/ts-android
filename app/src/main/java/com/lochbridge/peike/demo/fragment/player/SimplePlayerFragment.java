package com.lochbridge.peike.demo.fragment.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lochbridge.peike.demo.PlayerActivity;
import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.manager.SubtitleFileManager;
import com.lochbridge.peike.demo.model.SRTItem;
import com.lochbridge.peike.demo.util.Constants;

import java.lang.ref.WeakReference;
import java.util.List;

public class SimplePlayerFragment extends PlayerFragment {

    private static final String LOG_TAG = "SimplePlayerFragment";
    private TextView subTextView;
    private int mSubId;
    private SubMsgHandler subMsgHandler;
    private ReadFileThread thread;
    private TimerControlListener timerControlListener;


    public static SimplePlayerFragment newInstance(int subId) {

        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SUB_ID, subId);
        SimplePlayerFragment fragment = new SimplePlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSubId = getArguments().getInt(Constants.ARG_SUB_ID);
        if (getActivity() instanceof PlayerActivity) {
            timerControlListener = (PlayerActivity) getActivity();
        }
        subMsgHandler = new SubMsgHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player_simple, container, false);
        subTextView = (TextView) rootView.findViewById(R.id.simple_player_sub_text);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startPlaying();
        subTextView.setText(R.string.processing);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "Stop player.");
        subMsgHandler.removeCallbacksAndMessages(null);
        thread.stopFlag = true;
        try {
            thread.join();
            Log.d(LOG_TAG, "Thread stopped successfully");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerControlListener = null;
    }

    private void startPlaying() {
        thread = new ReadFileThread(this.mSubId);
        thread.start();
    }

    private void changeText(String newText) {
        CharSequence styledText = Html.fromHtml(newText);
        subTextView.setText(styledText);
    }

    @Override
    public void nextTapped() {
        thread.next();
    }

    @Override
    public void previousTapped() {
        thread.previous();
    }


    static class SubMsgHandler extends Handler {
        private final WeakReference<SimplePlayerFragment> mFragment;

        SubMsgHandler(SimplePlayerFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            SimplePlayerFragment fragment = mFragment.get();
            if (fragment != null) {
                fragment.changeText((String) msg.obj);
            }
        }
    }

    class ReadFileThread extends Thread {

        // read file to a list of SRTItem
        // then use handler to initiate
        // task to send data to main thread
        int subFileId;
        SRTItem currentNode;
        boolean stopFlag = false;
        boolean gotKickOff = false;
        List<SRTItem> subDataObjList;

        ReadFileThread(int subId) {
            subFileId = subId;
        }

        @Override
        public void run() {
            subDataObjList = SubtitleFileManager.getSRTItem(getActivity(), subFileId);
            Log.d(LOG_TAG, "SRT Item number: " + subDataObjList.size());
            if (subDataObjList == null || subDataObjList.isEmpty()) {
                sendMessage("Subtitle file is not valid.", false);
                return;
            }

            timerControlListener.startTimer();
            sendMessage("", false);

            currentNode = subDataObjList.get(0);
            try {
                iteration:
                do {
                    int sleepTime = getSleepTime();
                    long startTime = SystemClock.uptimeMillis(), endTime;
                    gotKickOff = false;
                    do {
                        if (stopFlag) break iteration;
                        Thread.sleep(10);
                        endTime = SystemClock.uptimeMillis();
                    } while (endTime - startTime < sleepTime && !gotKickOff);

                    timerControlListener.setTimer(currentNode.startTimeMilli);
                    sendMessage(currentNode.text, true);

                } while (currentNode != null && !stopFlag);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public synchronized void next() {
            gotKickOff = true;
        }

        public synchronized void previous() {
            // currentNode.previous == null: first node still has not shown
            if (currentNode.previous != null) {
                currentNode = currentNode.previous;
                // currentNode.previous == null: first node is on the screen
                if (currentNode.previous != null) {
                    currentNode = currentNode.previous;
                }
                gotKickOff = true; // kick off only after first node has shown
            }
        }

        private int getSleepTime() {
            int startTime = currentNode.previous == null ? 0 : currentNode.previous.startTimeMilli;
            int endTime = currentNode.startTimeMilli;
            return endTime - startTime;
        }

        private synchronized void sendMessage(String msgValue, boolean shouldMoveForward) {
            Message msg = subMsgHandler.obtainMessage(Constants.MSG_SRT_TEXT, msgValue);
            subMsgHandler.sendMessageAtTime(msg, SystemClock.uptimeMillis());
            if (shouldMoveForward) {
                currentNode = currentNode.next;
            }
        }

    }

}
