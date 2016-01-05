package com.lochbridge.peike.demo.fragment.player;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private void startPlaying() {
        thread = new ReadFileThread(getActivity(), this.mSubId);
        thread.start();
    }
    private void changeText(String newText) {
        String escapedTest = TextUtils.htmlEncode(newText);
        CharSequence styledText = Html.fromHtml(escapedTest);
        subTextView.setText(styledText);
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
        Context mContext;
        boolean stopFlag = false;

        ReadFileThread(Context context, int subId) {
            subFileId = subId;
            mContext = context;
        }

        @Override
        public void run() {
            List<SRTItem> subDataObjList = SubtitleFileManager.getSRTItem(mContext, subFileId);
            Log.d(LOG_TAG, "SRT Item number: " + subDataObjList.size());
            sendMessage("Processing done! Start Playing.", 0);
            for (final SRTItem srtItem : subDataObjList) {
                if (stopFlag) break;
                sendMessage(srtItem.text, srtItem.startTimeMilli);
            }
        }

        private void sendMessage(String msgValue, int delayedMilli) {
            Message msg = subMsgHandler.obtainMessage(Constants.MSG_SRT_TEXT, msgValue);
            subMsgHandler.sendMessageDelayed(msg, delayedMilli);
        }
    }

}
