package com.lochbridge.peike.demo.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.model.Subtitle;
import com.lochbridge.peike.demo.util.ResourceUtil;

import java.util.List;

/**
 * Created by PDai on 11/20/2015.
 */
public class SubtitleListAdapter extends BaseAdapter {
    private static final String LOG_TAG = "SubtitleListAdapter";
    List<Subtitle> mSubtitles;
    private LayoutInflater mInflater;
    private Context mContext;

    public SubtitleListAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void updateList(List<Subtitle> subtitles) {
        this.mSubtitles = subtitles;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSubtitles == null ? 0 : mSubtitles.size();
    }

    @Override
    public Object getItem(int position) {
        return mSubtitles == null ? null : mSubtitles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mSubtitles != null && !mSubtitles.isEmpty()) {
            ViewHolder holder;
            Log.d(LOG_TAG, "subtitle passed to adapter");
            if (convertView == null) {
                View v = mInflater.inflate(R.layout.list_item_subtitle, parent, false);
                holder = new ViewHolder();
                holder.subFileName = (TextView) v.findViewById(R.id.sub_file_name);
                holder.langImg = (ImageView) v.findViewById(R.id.lang_img);
                v.setTag(holder);

                convertView = v;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Subtitle subtitle = mSubtitles.get(position);
            holder.subFileName.setText(subtitle.fileName);
            int flagResId = ResourceUtil.getCountryFlagResId(mContext, subtitle.iso639);
            Log.d(LOG_TAG, "Flag resource ID: " + flagResId);
            if (flagResId != 0) {
                holder.langImg.setImageResource(flagResId);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        TextView subFileName;
        ImageView langImg;
    }
}
