package com.lochbridge.peike.demo.io;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by PDai on 12/3/2015.
 */
public abstract class StorageManager {
    public void writeToInternal(Context context,String filename, String fileContent) {
        try  {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(fileContent.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
