package com.lochbridge.peike.demo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.lochbridge.peike.demo.model.Movie;

/**
 * Created by Peike on 12/28/2015.
 */
public class MovieSubtitleDatabase extends SQLiteOpenHelper {
    private static MovieSubtitleDatabase singleton = null;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MovieSubtitle.db";
    public static final String MOVIES = "movies";
    public static final String SUBTITLES = "subtitles";
    private boolean isInitiated = false;

    private MovieSubtitleDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MovieSubtitleDatabase newInstance(Context context) {
        if (singleton == null) {
            singleton = new MovieSubtitleDatabase(context);
        }
        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MOVIES + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MovieSubtitleContract.Movies.IMDB_ID + " TEXT NOT NULL,"
                + MovieSubtitleContract.Movies.TITLE + " TEXT NOT NULL,"
                + MovieSubtitleContract.Movies.POSTER_URL + " TEXT NOT NULL,"
                + MovieSubtitleContract.Movies.BACKDROP_URL + " TEXT NOT NULL,"
                + MovieSubtitleContract.Movies.DOUBAN_RATING + " TEXT,"
                + MovieSubtitleContract.Movies.TOMATO_RATING + " TEXT,"
                + MovieSubtitleContract.Movies.IMDB_RATING + " TEXT)");
        db.execSQL("CREATE TABLE " + SUBTITLES + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MovieSubtitleContract.Movies.IMDB_ID + " TEXT NOT NULL,"
                + MovieSubtitleContract.Subtitles.FILE_ID + " INTEGER NOT NULL,"
                + MovieSubtitleContract.Subtitles.FILE_NAME + " TEXT NOT NULL,"
                + MovieSubtitleContract.Subtitles.FILE_SIZE + " INTEGER NOT NULL,"
                + MovieSubtitleContract.Subtitles.DURATION + " TEXT NOT NULL,"
                + MovieSubtitleContract.Subtitles.DOWNLOAD_COUNT + " INTEGER NOT NULL,"
                + MovieSubtitleContract.Subtitles.LANGUAGE + " TEXT NOT NULL,"
                + MovieSubtitleContract.Subtitles.ISO639 + " TEXT NOT NULL,"
                + MovieSubtitleContract.Subtitles.ADD_DATE + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + SUBTITLES);

        onCreate(db);
    }


}
