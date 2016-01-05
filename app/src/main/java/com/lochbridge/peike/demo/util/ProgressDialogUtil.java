package com.lochbridge.peike.demo.util;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by Peike on 1/4/2016.
 */
public class ProgressDialogUtil {
    private static ProgressDialog progressDialog;
    public static void show(Activity activity) {
        progressDialog = ProgressDialog.show(activity, "Downloading", "Please wait", false, false);
    }

    public static void hide() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }
}
