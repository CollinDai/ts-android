package com.lochbridge.peike.demo.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by PDai on 12/3/2015.
 */
public class StorageUtil {
    public static void writeToInternal(Context context, String fileName, String fileContent) {
        Writer out = null;
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            out = new BufferedWriter(new OutputStreamWriter(
                    fos, Charset.defaultCharset()));
            out.write(handleBOM(fileContent));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String handleBOM(String content) {
        if (content.charAt(0) == '\ufeff') {
            return content.substring(1);
        }
        return content;
    }

    public static String readFromInternal(Context context, String fileName) {
        String result = null;
        try {
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

    public static FileInputStream readStreamFromInternal(Context context, String fileName) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fis;
    }
}
