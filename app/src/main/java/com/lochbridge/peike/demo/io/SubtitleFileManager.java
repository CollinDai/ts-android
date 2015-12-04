package com.lochbridge.peike.demo.io;

import android.content.Context;
import android.util.Log;

import com.lochbridge.peike.demo.network.NetworkManager;
import com.lochbridge.peike.demo.util.StorageUtil;

import java.io.File;

/**
 * Created by PDai on 12/2/2015.
 */
public class SubtitleFileManager {

    private static final String LOG_TAG = "SubtitleFileManager";

    public static void downloadSubtitle(final Context context, final int subId, NetworkManager.Callback<String> callback) {
        NetworkManager.download(context, subId, callback);
    }

    public static void putSubtitle(Context context, int subId, String subContent) {
        StorageUtil.writeToInternal(context, String.valueOf(subId), subContent);
    }

    public static String getSubtitle(Context context, int subId) {
       return StorageUtil.readFromInternal(context, String.valueOf(subId));
    }

    public static boolean isSubtitleExist(Context context, int subId) {
        String fileName = String.valueOf(subId);
        File subFile = context.getFileStreamPath(fileName);
        boolean exist = subFile.exists();
        Log.e(LOG_TAG, "Sub exists " + exist);
        return exist;
    }

    public static boolean deleteSubtitle(Context context, int subId) {
       return context.deleteFile(String.valueOf(subId));
    }
}
