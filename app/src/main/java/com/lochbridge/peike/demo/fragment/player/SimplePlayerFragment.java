package com.lochbridge.peike.demo.fragment.player;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
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

/**
 * Created by Peike on 12/7/2015.
 */
public class SimplePlayerFragment extends PlayerFragment {
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
        startPlaying();
        changeText(String.valueOf(mSubId));
    }

    private void startPlaying() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                changeText("Bonjour");
//            }
//        }, 5000);
        thread = new ReadFileThread(getActivity(), this.mSubId);
        thread.start();
    }

    private void changeText(String newText) {
        String escapedTest = TextUtils.htmlEncode(newText);
        CharSequence styledText = Html.fromHtml(escapedTest);
        subTextView.setText(styledText);
    }

    @Override
    public void onStop() {
        super.onStop();
        subMsgHandler.removeCallbacksAndMessages(null);
        thread.stopFlag = true;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            String subtitleContent = SubtitleFileManager.getSubtitle(mContext, subFileId);
            List<SRTItem> subDataObjList = SubtitleFileManager.convertToList(subtitleContent);
            for (final SRTItem srtItem : subDataObjList) {
                if (stopFlag) break;
                SimplePlayerFragment.this.subMsgHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SimplePlayerFragment.this.changeText(srtItem.text);
                    }
                }, srtItem.startTimeMilli);
            }
        }
    }

}
