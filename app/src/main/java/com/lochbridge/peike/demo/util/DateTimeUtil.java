package com.lochbridge.peike.demo.util;

/**
 * Created by PDai on 12/8/2015.
 */
public class DateTimeUtil {
    public static int timecodeToMillisecond(String timecode) {
        int commaIdx = timecode.lastIndexOf(',');
        String seconds = timecode.substring(0, commaIdx);
        String milliSeconds = timecode.substring(commaIdx + 1);
        String[] times = seconds.split(":");

        int result = 3600000 * Integer.valueOf(times[0]) +
                        60000 * Integer.valueOf(times[1]) +
                        1000 * Integer.valueOf(times[2]);

        return result + Integer.valueOf(milliSeconds);

    }
}
