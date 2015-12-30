package com.lochbridge.peike.demo.views;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.database.MovieSubtitleContract;

/**
 * Created by Peike on 12/29/2015.
 */
public class LocalMovieAdapter extends CursorAdapter {
    public LocalMovieAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String title = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Movies.TITLE));
        String imdbId = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Movies.IMDB_ID));
        String posterUrl = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Movies.POSTER_URL));
        String doubanRaing = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Movies.DOUBAN_RATING));
        String imdbRating = cursor.getString(cursor.getColumnIndexOrThrow(MovieSubtitleContract.Movies.IMDB_RATING));

        TextView titleView = (TextView) view.findViewById(R.id.gridview_title);
        TextView doubanRatingView = (TextView) view.findViewById(R.id.douban_rating);
        TextView imdbRatingView = (TextView) view.findViewById(R.id.imdb_rating);

        titleView.setText(title);
        doubanRatingView.setText(doubanRaing);
        imdbRatingView.setText(imdbRating);
        view.setTag(imdbId);
    }
}
