package com.lochbridge.peike.demo.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lochbridge.peike.demo.model.SRTItem;
import com.lochbridge.peike.demo.network.NetworkManager;
import com.lochbridge.peike.demo.util.DateTimeUtil;
import com.lochbridge.peike.demo.util.StorageUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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


    public static List<SRTItem> getSRTItem(Context context, int subId) {
        List<SRTItem> result = new ArrayList<>();
        try {
            String fileName = String.valueOf(subId);
            FileInputStream fis = StorageUtil.readStreamFromInternal(context, fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            Log.d(LOG_TAG, "Open file to reader: " + fileName);
            String line;
            String prevLine = "";
            SRTItem srtItem = null;
            int counter = 0;
            while ((line = br.readLine()) != null) {
                if (isItemNumber(line, prevLine)) {
                    if (srtItem != null) {
                        result.add(srtItem);
                    }
                    srtItem = new SRTItem();
                    srtItem.number = Integer.valueOf(line);
                } else if (isTimeCode(line) && srtItem != null) {
                    int splitterIdx = line.indexOf("-->");
                    String startTimecode = line.substring(0, splitterIdx).trim();
                    String endTimecode = line.substring(splitterIdx + 3).trim();
                    srtItem.startTimeMilli = DateTimeUtil.timecodeToMillisecond(startTimecode);
                    srtItem.endTimeMilli = DateTimeUtil.timecodeToMillisecond(endTimecode);
                } else if (srtItem != null) {
                    srtItem.text = srtItem.text == null ? line : srtItem.text + '\n' + line;
                }
                prevLine = line;
            }
            if (srtItem != null)
                result.add(srtItem);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
            return new ArrayList<>();
        }
        return result;
    }

    private static boolean isItemNumber(String line, String prevLine) {
        if (line == null || line.isEmpty()) return false;
        for (char c : line.toCharArray()) {
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return "".equals(prevLine);
    }

    private static boolean isTimeCode(String line) {
        if (line == null || line.isEmpty()) return false;
        return line.matches("\\d{2}:\\d{2}:\\d{2},\\d{3} --> \\d{2}:\\d{2}:\\d{2},\\d{3}");
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
