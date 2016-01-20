package com.lochbridge.peike.demo.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by PDai on 11/5/2015.
 */
public class Movie implements Parcelable {
    public String title;
    public String posterUrl;
    public String backdropUrl;
    public String imdbId;
    public String doubanRating;
    public String imdbRating;
    public String tomatoRating;
    public List<Subtitle> subtitles;

    public Movie() {}

    protected Movie(Parcel in) {
        title = in.readString();
        posterUrl = in.readString();
        backdropUrl = in.readString();
        imdbId = in.readString();
        doubanRating = in.readString();
        imdbRating = in.readString();
        tomatoRating = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterUrl);
        dest.writeString(backdropUrl);
        dest.writeString(imdbId);
        dest.writeString(doubanRating);
        dest.writeString(imdbRating);
        dest.writeString(tomatoRating);
    }
}
