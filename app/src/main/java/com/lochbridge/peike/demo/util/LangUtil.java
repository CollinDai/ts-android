package com.lochbridge.peike.demo.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PDai on 11/23/2015.
 *
 * Singleton class because of context used to access assets.
 */
public class LangUtil {
    private static final String iso639FileUri = "iso639.csv";
    private static final String LOG_TAG = "LangUtil";
    private static Map<String, String> nameISOMap;
    private static Context mContext = null;
    private static LangUtil singleton = null;

    private LangUtil(Context context) {
        mContext = context;
        BufferedReader reader = null;
        nameISOMap = new HashMap<>();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(iso639FileUri)));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                String[] tmp = mLine.split("\t");
                nameISOMap.put(tmp[2].toLowerCase(), tmp[0]);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Fail to process csv file");
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    Log.d(LOG_TAG, "Successfully read " + nameISOMap.size() + " entries");
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Fail to close reader");
                }
            }
        }
    }


    public static LangUtil newInstance(Context context) {
        if (singleton == null) {
            singleton = new LangUtil(context);
        }
        return singleton;
    }

    public String nameToISO639_2(String langName) {
        Log.d(LOG_TAG, "Convert " + langName);
        return nameISOMap.get(langName);
    }

    public List<String> nameToISO639_2(List<String> names) {
        List<String> isoCodes = new ArrayList<>();
        for (String name : names) {
            String isoCode = nameToISO639_2(name.toLowerCase());
            if (isoCode != null) {
                isoCodes.add(isoCode);
            }
        }
        return isoCodes;
    }
}
