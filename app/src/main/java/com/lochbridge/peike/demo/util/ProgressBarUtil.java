package com.lochbridge.peike.demo.util;

import android.app.Activity;
import android.view.View;

import com.lochbridge.peike.demo.R;

/**
 * Created by Peike on 1/2/2016.
 */
public class ProgressBarUtil {
    public static void showLoadingCircle(Activity activity) {
        View progressOverlay = activity.findViewById(R.id.loadingPanel);
        if (progressOverlay != null) {
            progressOverlay.setVisibility(View.VISIBLE);
        }
    }

    public static void hideLoadingCircle(Activity activity) {
        View progressOverlay = activity.findViewById(R.id.loadingPanel);
        if (progressOverlay != null) {
            progressOverlay.setVisibility(View.GONE);
        }
    }
}
