package com.lochbridge.peike.demo.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lochbridge.peike.demo.DetailActivity;
import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.manager.SubtitleFileManager;
import com.lochbridge.peike.demo.model.Subtitle;
import com.lochbridge.peike.demo.network.NetworkManager;
import com.lochbridge.peike.demo.util.ProgressDialogUtil;
import com.lochbridge.peike.demo.util.ResourceUtil;
import com.lochbridge.peike.demo.util.StorageUtil;

import java.util.List;

/**
 * Created by PDai on 11/20/2015.
 */
public class SubtitleListAdapter extends BaseAdapter {
    private static final String LOG_TAG = "SubtitleListAdapter";
    private List<Subtitle> mSubtitles;
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
            if (convertView == null) {
                View v = mInflater.inflate(R.layout.list_item_subtitle, parent, false);
                holder = new ViewHolder();
                holder.subFileName = (TextView) v.findViewById(R.id.sub_file_name);
                holder.langImg = (ImageView) v.findViewById(R.id.lang_img);
                holder.downloadIcon = (ImageView) v.findViewById(R.id.download_icon);
                v.setTag(holder);

                convertView = v;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Subtitle subtitle = mSubtitles.get(position);
            holder.subFileName.setText(subtitle.fileName);
            holder.subtitleIdx = position;
            if (SubtitleFileManager.isSubtitleExist(mContext, subtitle.fileId)) {
                holder.downloadIcon.setVisibility(View.INVISIBLE);
            } else {
                holder.downloadIcon.setTag(position);
                holder.downloadIcon.setOnClickListener(new DownloadClickListener());
            }
            int flagResId = ResourceUtil.getCountryFlagResId(mContext, subtitle.iso639);
            if (flagResId != 0) {
                holder.langImg.setImageResource(flagResId);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        TextView subFileName;
        ImageView downloadIcon;
        ImageView langImg;
        int subtitleIdx;
    }

    class DownloadClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            int index = (Integer) v.getTag();
            final Subtitle subtitle = mSubtitles.get(index);
            final int subId = subtitle.fileId;
            ProgressDialogUtil.show((Activity) mContext);
            SubtitleFileManager.downloadSubtitle(mContext, subId, new NetworkManager.Callback<String>() {
                @Override
                public void onResponse(String s) {
                    SubtitleFileManager.putSubtitle(mContext, subId, s);
                    StorageUtil.writeSubtitleToDB(mContext, subtitle);
                    if (mContext instanceof DetailActivity) {
                        DetailActivity activity = (DetailActivity) mContext;
                        activity.persistMovieToDB();
                    }
                    ProgressDialogUtil.hide();
                    v.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
