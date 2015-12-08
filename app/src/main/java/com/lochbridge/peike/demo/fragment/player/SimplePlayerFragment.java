package com.lochbridge.peike.demo.fragment.player;

import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.lochbridge.peike.demo.model.SRTItem;

/**
 * Created by Peike on 12/7/2015.
 */
public class SimplePlayerFragment extends PlayerFragment {
    private TextView subTextView;
    public void changeText(String newText) {
        String escapedTest = TextUtils.htmlEncode(newText);
        CharSequence styledText = Html.fromHtml(escapedTest);
        subTextView.setText(styledText);
    }

}
