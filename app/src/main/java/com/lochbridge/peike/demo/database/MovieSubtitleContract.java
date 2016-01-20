package com.lochbridge.peike.demo.database;

import android.provider.BaseColumns;

/**
 * Created by Peike on 12/28/2015.
 */
public class MovieSubtitleContract {

    private MovieSubtitleContract(){}
    public static class Movies implements BaseColumns {
        public static final String IMDB_ID = "imdb_id";
        public static final String POSTER_URL = "poster_url";
        public static final String BACKDROP_URL = "backdrop_url";
        public static final String TITLE = "title";
        public static final String DOUBAN_RATING = "douban_rating";
        public static final String TOMATO_RATING = "tomato_rating";
        public static final String IMDB_RATING = "imdb_rating";

    }

    public static class Subtitles implements BaseColumns {
        public static final String IMDB_ID = "imdb_id";
        public static final String FILE_ID = "file_id";
        public static final String FILE_NAME = "file_name";
        public static final String FILE_SIZE = "file_size";
        public static final String DURATION = "duration";
        public static final String DOWNLOAD_COUNT = "download_count";
        public static final String LANGUAGE = "language";
        public static final String ISO639 = "ISO639";
        public static final String ADD_DATE = "add_date";
    }
}
