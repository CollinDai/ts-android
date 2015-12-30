package com.lochbridge.peike.demo.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.model.Movie;
import com.lochbridge.peike.demo.network.NetworkManager;

import java.util.List;

/**
 * Created by PDai on 10/13/2015.
 */
public class MovieListAdapter extends BaseAdapter {

    private static final String LOG_TAG = "MovieListAdapter";
    private LayoutInflater mInflater;
    private List<Movie> mHotMovies;

    public MovieListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void updateList(List<Movie> movies) {
        this.mHotMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mHotMovies == null ? 0 : mHotMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return mHotMovies == null ? null : mHotMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mHotMovies != null && !mHotMovies.isEmpty()) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                View v = this.mInflater.inflate(R.layout.list_item, parent, false);
                v.setTag(holder);

                holder.primaryText = (TextView) v.findViewById(R.id.primary_text);
                holder.imdbRating = (TextView) v.findViewById(R.id.imdb_rating);
                holder.doubanRating = (TextView) v.findViewById(R.id.douban_rating);
                holder.imageView = (NetworkImageView) v.findViewById(R.id.item_avatar);
                convertView = v;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Movie m = mHotMovies.get(position);
            holder.primaryText.setText(m.title);
            holder.imdbRating.setText(m.imdbRating);
            holder.doubanRating.setText(m.doubanRating);
            holder.primaryText.setTag(m.imdbId);
            if (URLUtil.isValidUrl(m.posterUrl)) {
                NetworkManager.setPoster(holder.imageView, m.posterUrl);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        NetworkImageView imageView;
        TextView primaryText;
        TextView imdbRating;
        TextView doubanRating;
    }
}
