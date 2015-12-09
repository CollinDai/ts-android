package com.lochbridge.peike.demo.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lochbridge.peike.demo.model.SRTItem;
import com.lochbridge.peike.demo.network.NetworkManager;
import com.lochbridge.peike.demo.util.DateTimeUtil;
import com.lochbridge.peike.demo.util.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PDai on 12/2/2015.
 */
public class SubtitleFileManager {

    private static final String LOG_TAG = "SubtitleFileManager";

    private static boolean threadStopFlag = false;

    public static void downloadSubtitle(final Context context, final int subId, NetworkManager.Callback<String> callback) {
        NetworkManager.download(context, subId, callback);
    }

    public static void putSubtitle(Context context, int subId, String subContent) {
        StorageUtil.writeToInternal(context, String.valueOf(subId), subContent);
    }

    public static String getSubtitle(Context context, int subId) {
        return StorageUtil.readFromInternal(context, String.valueOf(subId));
    }

    public static List<SRTItem> convertToList(String subtitleContent) {
        String[] lines = subtitleContent.split("\\r?\\n");
        List<SRTItem> subDataObjList = new ArrayList<>();
        SRTItem srtItem = null;
        for (int i = 0; i < lines.length; ++i) {
            if (threadStopFlag) break;
            String line = lines[i].trim();
            Log.d(LOG_TAG, line);
            if (line.equals("")) {
                if (srtItem != null) {
                    subDataObjList.add(srtItem);
                }
            } else if (TextUtils.isDigitsOnly(line) && (i - 1 < 0 || (lines[i - 1].trim().equals("")))) {
                srtItem = new SRTItem();
                srtItem.number = Integer.valueOf(line);
            } else if (line.matches("\\d{2}:\\d{2}:\\d{2},\\d{3} --> \\d{2}:\\d{2}:\\d{2}:,\\d{3}")) {
                int splitterIdx = line.indexOf("-->");
                String startTimecode = line.substring(0, splitterIdx).trim();
                String endTimecode = line.substring(splitterIdx + 3).trim();
                srtItem.startTimeMilli = DateTimeUtil.timecodeToMillisecond(startTimecode);
                Log.d(LOG_TAG, "Start Time" + srtItem.startTimeMilli);
                srtItem.endTimeMilli = DateTimeUtil.timecodeToMillisecond(endTimecode);
            } else {
                srtItem.text = srtItem.text == null ? line : srtItem.text + line;
            }
            subDataObjList.add(srtItem);
        }
        return subDataObjList;
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
