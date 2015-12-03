package com.lochbridge.peike.demo.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.model.Subtitle;
import com.lochbridge.peike.demo.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PDai on 11/25/2015.
 */
public class SubtitleDialogFragment extends DialogFragment {
    private static final String LOG_TAG = "SubtitleDialogFragment";
    private LayoutInflater mInflater;
    private Subtitle mSubtitle;
    private Context context;

    public void setSubtitle(Subtitle subtitle) {
        this.mSubtitle = subtitle;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (mSubtitle != null) {
            this.mInflater = getActivity().getLayoutInflater();
            View layout = mInflater.inflate(R.layout.dialog_subtitle, null);
            Button startButton  = (Button) layout.findViewById(R.id.start);
            Button downDelButton  = (Button) layout.findViewById(R.id.download_or_delete);

            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStartSubClick(v);
                }
            });

            downDelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDownOrDelClicked(v);
                }
            });

            ListView subDetailList = (ListView) layout.findViewById(android.R.id.list);
            SubDetailAdapter adapter = new SubDetailAdapter(getDetailListForAdapter());
            subDetailList.setAdapter(adapter);
            builder.setView(layout);
        }
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private List<Pair<String, String>> getDetailListForAdapter() {
        return new ArrayList<Pair<String, String>>() {{
            add(new Pair<>("File Name", mSubtitle.fileName));
            add(new Pair<>("Language", mSubtitle.language));
            add(new Pair<>("File Size", convertByteToKB(mSubtitle.fileSize)));
            add(new Pair<>("Duration", mSubtitle.duration));
            add(new Pair<>("Download Count", mSubtitle.downloadCount));

        }};
    }

    private String convertByteToKB(String b) {
        double byteValue = Double.valueOf(b);
        return String.format("%.2f", byteValue / 1024D) + " KB";
    }

    private void onStartSubClick(View view) {

    }

    private void onDownOrDelClicked(View view) {
        int subId = SubtitleDialogFragment.this.mSubtitle.fileId;
        // TODO show progress indicator in this button view

    }

    class SubDetailAdapter extends BaseAdapter {
        List<Pair<String, String>> mDetails;
        SubDetailAdapter(List<Pair<String, String>> details) {
            this.mDetails = details;
        }
        @Override
        public int getCount() {
            return mDetails.size();
        }
        @Override
        public Object getItem(int position) {
            return mDetails.get(position);
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_sub_detail, parent, false);
                holder = new ViewHolder();
                holder.detailName = (TextView) convertView.findViewById(R.id.detail_name);
                holder.detailValue = (TextView) convertView.findViewById((R.id.detail_value));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.detailName.setText(mDetails.get(position).first);
            holder.detailValue.setText(mDetails.get(position).second);
            return convertView;
        }

        class ViewHolder {
            TextView detailName;
            TextView detailValue;
        }
    }
}
