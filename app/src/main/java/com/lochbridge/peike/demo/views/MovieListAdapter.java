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
    private Context context;

    public MovieListAdapter(Context context) {
        this.context = context;
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
            ViewHolder holder;
            if (convertView == null) {
                View v = this.mInflater.inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.rankingNumber = (TextView) v.findViewById(R.id.ranking_number);
                holder.primaryText = (TextView) v.findViewById(R.id.primary_text);
                holder.imdbRating = (TextView) v.findViewById(R.id.imdb_rating);
                holder.tomatoRating = (TextView) v.findViewById(R.id.tomato_rating);
                holder.imageView = (NetworkImageView) v.findViewById(R.id.item_avatar);
                v.setTag(holder);
                convertView = v;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Movie m = mHotMovies.get(position);
            String ranking = String.valueOf(position + 1) + ". ";
            holder.rankingNumber.setText(ranking);
            holder.primaryText.setText(m.title);
            holder.imdbRating.setText(m.imdbRating);
            holder.tomatoRating.setText(m.tomatoRating);
            holder.primaryText.setTag(m.imdbId);
            holder.imageView.setTag(m.backdropUrl);
            if (URLUtil.isValidUrl(m.posterUrl)) {
                NetworkManager.setPoster(context, holder.imageView, m.posterUrl);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        NetworkImageView imageView;
        TextView rankingNumber;
        TextView primaryText;
        TextView imdbRating;
        TextView tomatoRating;

    }
}
