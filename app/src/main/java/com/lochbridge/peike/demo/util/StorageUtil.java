package com.lochbridge.peike.demo.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by PDai on 12/3/2015.
 */
public class StorageUtil {
    public static void writeToInternal(Context context,String fileName, String fileContent) {
        try  {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(fileContent.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromInternal(Context context, String fileName) {
        String result = null;
        try  {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
