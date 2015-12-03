package com.lochbridge.peike.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PDai on 11/19/2015.
 */
public class Subtitle{
    public String fileName;
    public String language;
    public String fileSize;
    public String duration;
    public String downloadCount;
    public int fileId;
    // used only for select country flag
    // has nothing to do with language
    public String iso639;
    public String addDate;

//    protected Subtitle(Parcel in) {
//        fileName = in.readString();
//        language = in.readString();
//        fileSize = in.readString();
//        downloadLink = in.readString();
//        duration = in.readString();
//        downloadCount = in.readString();
//        fileId = in.readInt();
//        iso639 = in.readString();
//        iso639_2 = in.readString();
//    }
//
//    public static final Creator<Subtitle> CREATOR = new Creator<Subtitle>() {
//        @Override
//        public Subtitle createFromParcel(Parcel in) {
//            return new Subtitle(in);
//        }
//
//        @Override
//        public Subtitle[] newArray(int size) {
//            return new Subtitle[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(fileName);
//        dest.writeString(language);
//        dest.writeString(fileSize);
//        dest.writeString(downloadLink);
//        dest.writeString(duration);
//        dest.writeString(downloadCount);
//        dest.writeInt(fileId);
//        dest.writeString(iso639);
//        dest.writeString(iso639_2);
//    }
}
