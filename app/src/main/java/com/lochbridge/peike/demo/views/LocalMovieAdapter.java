package com.lochbridge.peike.demo.views;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.database.MovieSubtitleContract;
import com.lochbridge.peike.demo.model.Movie;
import com.lochbridge.peike.demo.network.NetworkManager;

import java.util.List;

/**
 * Created by Peike on 12/29/2015.
 */
public class LocalMovieAdapter extends BaseAdapter {

    private static final String LOG_TAG = "LocalMovieAdapter";
    private List<Movie> localMovies;
    private LayoutInflater layoutInflater;
    private Context context;
    public LocalMovieAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void udpateGrid(List<Movie> updatedLocalMovies) {
        this.localMovies = updatedLocalMovies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return localMovies == null ? 0 : localMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return localMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.localMovies != null && !this.localMovies.isEmpty()) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                View view = layoutInflater.inflate(R.layout.grid_item, parent, false);
                holder.posterImgView = (NetworkImageView) view.findViewById(R.id.item_avatar);
                holder.titleView = (TextView) view.findViewById(R.id.primary_text);
                holder.doubanRatingView = (TextView) view.findViewById(R.id.douban_rating);
                holder.imdbRatingView = (TextView) view.findViewById(R.id.imdb_rating);
                convertView = view;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Movie movie = localMovies.get(position);
            holder.titleView.setText(movie.title);
            holder.doubanRatingView.setText(movie.doubanRating);
            holder.imdbRatingView.setText(movie.imdbRating);
            holder.titleView.setTag(movie.imdbId);
            holder.posterImgView.setTag(movie.backdropUrl);
            if (URLUtil.isValidUrl(movie.posterUrl)) {
                NetworkManager.setPoster(context, holder.posterImgView, movie.posterUrl);
            }
        }
        return convertView;
    }

    class ViewHolder {
        NetworkImageView posterImgView;
        TextView titleView;
        TextView doubanRatingView;
        TextView imdbRatingView;
    }
}
