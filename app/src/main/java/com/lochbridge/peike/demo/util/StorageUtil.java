package com.lochbridge.peike.demo.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lochbridge.peike.demo.database.MovieSubtitleContract;
import com.lochbridge.peike.demo.database.MovieSubtitleDatabase;
import com.lochbridge.peike.demo.model.Movie;
import com.lochbridge.peike.demo.model.Subtitle;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PDai on 12/3/2015.
 */
public class StorageUtil {
    private static final String LOG_TAG = "StorageUtil";

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


    /*
     * Database manipulations below
     */

    public static Cursor getReadableMovieCursor(Context context) {
        MovieSubtitleDatabase openHelper = MovieSubtitleDatabase.newInstance(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        //TODO refactor this
        Cursor cursor = db.rawQuery("SELECT * FROM movies WHERE imdb_id IN (SELECT DISTINCT IMDB_ID FROM subtitles)", null);
        return cursor;
    }

    public static List<Movie> readAllLocalMovies(Context context) {
        MovieSubtitleDatabase openHelper = MovieSubtitleDatabase.newInstance(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM movies WHERE imdb_id IN (SELECT DISTINCT IMDB_ID FROM subtitles)", null);
        List<Movie> result = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Movie m = new Movie();
                m.imdbId = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Movies.IMDB_ID));
                m.title = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Movies.TITLE));
                m.imdbRating = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Movies.IMDB_RATING));
                m.tomatoRating = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Movies.TOMATO_RATING));
                m.posterUrl = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Movies.POSTER_URL));
                m.backdropUrl = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Movies.BACKDROP_URL));
                result.add(m);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return result;
    }

    public static List<Subtitle> readAllSubtitlesFromDBByImdbId(Context context, String imdbId) {
        MovieSubtitleDatabase openHelper = MovieSubtitleDatabase.newInstance(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query(MovieSubtitleDatabase.SUBTITLES, null, "IMDB_ID=?", new String[]{imdbId}, null, null, null);
        List<Subtitle> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Subtitle newSub = new Subtitle();
                newSub.imdbId = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Subtitles.IMDB_ID));
                newSub.fileId = cursor.getInt(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Subtitles.FILE_ID));
                newSub.fileName = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Subtitles.FILE_NAME));
                newSub.fileSize = cursor.getInt(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Subtitles.FILE_SIZE));
                newSub.duration = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Subtitles.DURATION));
                newSub.downloadCount = cursor.getInt(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Subtitles.DOWNLOAD_COUNT));
                newSub.language = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Subtitles.LANGUAGE));
                newSub.iso639 = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Subtitles.ISO639));
                newSub.addDate = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Subtitles.ADD_DATE));
                result.add(newSub);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result;
    }

    public static void writeSubtitleToDB(Context context, Subtitle mSubtitle) {
        MovieSubtitleDatabase openHelper = MovieSubtitleDatabase.newInstance(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieSubtitleContract.Subtitles.IMDB_ID, mSubtitle.imdbId);
        values.put(MovieSubtitleContract.Subtitles.FILE_ID, mSubtitle.fileId);
        values.put(MovieSubtitleContract.Subtitles.FILE_NAME, mSubtitle.fileName);
        values.put(MovieSubtitleContract.Subtitles.FILE_SIZE, mSubtitle.fileSize);
        values.put(MovieSubtitleContract.Subtitles.DURATION, mSubtitle.duration);
        values.put(MovieSubtitleContract.Subtitles.DOWNLOAD_COUNT, mSubtitle.downloadCount);
        values.put(MovieSubtitleContract.Subtitles.LANGUAGE, mSubtitle.language);
        values.put(MovieSubtitleContract.Subtitles.ISO639, mSubtitle.iso639);
        values.put(MovieSubtitleContract.Subtitles.ADD_DATE, mSubtitle.addDate);
        long rowId = db.insertOrThrow(MovieSubtitleDatabase.SUBTITLES, null, values);
    }

    public static void writeMovieToDB(Context context, Movie movie) {
        MovieSubtitleDatabase openHelper = MovieSubtitleDatabase.newInstance(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieSubtitleContract.Movies.IMDB_ID, movie.imdbId);
        values.put(MovieSubtitleContract.Movies.TITLE, movie.title);
        values.put(MovieSubtitleContract.Movies.POSTER_URL, movie.posterUrl);
        values.put(MovieSubtitleContract.Movies.BACKDROP_URL, movie.backdropUrl);
        values.put(MovieSubtitleContract.Movies.TOMATO_RATING, movie.tomatoRating);
        values.put(MovieSubtitleContract.Movies.IMDB_RATING, movie.imdbRating);
        long rowId = db.insertOrThrow(MovieSubtitleDatabase.MOVIES, null, values);
        Log.d(LOG_TAG, "Movie is inserted to row " + rowId);
    }

    public static void deleteSubtitle(Context context, String imdbId, int subId) {
        MovieSubtitleDatabase openHelper = MovieSubtitleDatabase.newInstance(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.delete(MovieSubtitleDatabase.SUBTITLES,
                MovieSubtitleContract.Subtitles.FILE_ID + "= ?",
                new String[]{String.valueOf(subId)});
        // TODO: 12/30/2015 check if movie still has subtitle, if not then remove movie
        Cursor cursor = db.rawQuery("SELECT * FROM subtitles WHERE IMDB_ID = ? ", new String[]{imdbId});
        if (cursor != null && cursor.getCount() == 0) {
            int rows = db.delete(MovieSubtitleDatabase.MOVIES,
                    MovieSubtitleContract.Movies.IMDB_ID + "= ?",
                    new String[]{imdbId});
            Log.d(LOG_TAG, rows + " rows of movies are deleted");
        }
        cursor.close();
    }

    public static void deleteMovie(Context context, String imdbId) {

    }

    public static boolean isMovieExist(Context context, String mImdbID) {
        MovieSubtitleDatabase openHelper = MovieSubtitleDatabase.newInstance(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM movies WHERE IMDB_ID = ? ", new String[]{mImdbID});
        return cursor != null && cursor.getCount() > 0;
    }
}
