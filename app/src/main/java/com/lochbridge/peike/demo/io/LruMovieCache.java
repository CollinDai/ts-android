package com.lochbridge.peike.demo.io;

import android.content.Context;
import android.util.LruCache;

import com.lochbridge.peike.demo.model.Movie;
import com.lochbridge.peike.demo.model.Subtitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PDai on 12/2/2015.
 */
public class LruMovieCache {
    private static LruCache<String, Movie> movieCache;

    static {
        movieCache = new LruCache<>(10);
    }
    public static void putMovieList(List<Movie> movies) {
        if (movies != null) {
            for (Movie m : movies) {
                if (movieCache.get(m.imdbId) != null) continue;
                movieCache.put(m.imdbId, m);
            }
        }
    }

    public static List<Movie> getMovieList() {
        return new ArrayList<>(movieCache.snapshot().values());
    }

    public static Movie getMovie(String imdbId) {
        return movieCache.get(imdbId);
    }

    public static void putSubtitleList(String imdbId, List<Subtitle> subtitles) {
        Movie movie = movieCache.get(imdbId);
        movie.subtitles = subtitles;
//        movieCache.put(imdbId, movie);
    }

    public static List<Subtitle> getSubtitleList(String imdbId) {
        Movie movie = movieCache.get(imdbId);
        return movie.subtitles;
    }

    public static Subtitle getSubtitle(String imdbId, int subId) {
        Movie movie = movieCache.get(imdbId);
        Subtitle result = null;
        if (movie.subtitles != null) {
            for (Subtitle s : movie.subtitles) {
                if (s.fileId == subId) {
                    result = s;
                    break;
                }
            }
        }
        return result;
    }

    public static void persistCache(Context context) {
    }
}
