package com.lochbridge.peike.demo.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lochbridge.peike.demo.PlayerActivity;
import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.manager.SubtitleFileManager;
import com.lochbridge.peike.demo.model.Subtitle;
import com.lochbridge.peike.demo.network.NetworkManager;
import com.lochbridge.peike.demo.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PDai on 11/25/2015.
 * Subtitle detail dialog.
 */
public class SubtitleDialogFragment extends DialogFragment {
    private static final String LOG_TAG = "SubtitleDialogFragment";
    private LayoutInflater mInflater;
    private Button mStartButton;
    private Button mDownDelButton;
    private ImageView mDownloadIcon;
    private Subtitle mSubtitle;
    private boolean isSubExist;

    public void setArg(ImageView downIcon, Subtitle subtitle) {
        this.mSubtitle = subtitle;
        this.mDownloadIcon = downIcon;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (mSubtitle != null) {
            this.mInflater = getActivity().getLayoutInflater();
            View layout = mInflater.inflate(R.layout.dialog_subtitle, null);
            mStartButton = (Button) layout.findViewById(R.id.start);
            mDownDelButton = (Button) layout.findViewById(R.id.download_or_delete);

            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStartSubClick();
                }
            });

            mDownDelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDownOrDelClicked();
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onViewCreated()");
        // TODO check if sub file exists
        // if it does, change download button to delete button and enable start
        // otherwise do nothing.
        if (SubtitleFileManager.isSubtitleExist(getActivity(), mSubtitle.fileId)) {
            toggleSubDialogButtons(true);
        } else {
            toggleSubDialogButtons(false);
        }
    }

    private List<Pair<String, String>> getDetailListForAdapter() {
        return new ArrayList<Pair<String, String>>() {{
            add(new Pair<>("File Name", mSubtitle.fileName));
            add(new Pair<>("Language", mSubtitle.language));
            add(new Pair<>("File Size", convertByteToKB(mSubtitle.fileSize)));
            add(new Pair<>("Duration", mSubtitle.duration));
            add(new Pair<>("Download Count", mSubtitle.downloadCount));
            add(new Pair<>("Add Date", mSubtitle.addDate));
        }};
    }

    private String convertByteToKB(String b) {
        double byteValue = Double.valueOf(b);
        return String.format("%.2f", byteValue / 1024D) + " KB";
    }


    /**
     * TODO return command to activity to start player
     * instead of starting from this fragment
     */
    private void onStartSubClick() {
        // start sub view activity, pass the sub file name
        // as intent arguments.
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(Constants.EXTRA_SUB_ID, mSubtitle.fileId);
        startActivity(intent);
    }

    private void onDownOrDelClicked() {
        final int subId = this.mSubtitle.fileId;
        // TODO show progress indicator in this button view
        if (isSubExist) {
            boolean result = SubtitleFileManager.deleteSubtitle(getActivity(), subId);
            if (result) {
                toggleSubDialogButtons(false);
            }
            Toast.makeText(getActivity(), "Delete " + (result ? "succeed!" : "failed!"), Toast.LENGTH_SHORT).show();
        } else {
            SubtitleFileManager.downloadSubtitle(getActivity(), subId, new NetworkManager.Callback<String>() {
                @Override
                public void onResponse(String s) {
                    if (s.charAt(0) == '\uFEFF') {
                        Log.d(LOG_TAG, "Downloaded file has BOM.");
                    }
                    SubtitleFileManager.putSubtitle(getActivity(), subId, s);
                    toggleSubDialogButtons(true);
                }
            });
        }

    }

    private void toggleSubDialogButtons(boolean hasSub) {
        if (hasSub) {
            this.isSubExist = true;
            mDownDelButton.setText(R.string.delete);
            mStartButton.setEnabled(true);
            mDownloadIcon.setVisibility(View.INVISIBLE);
        } else {
            this.isSubExist = false;
            mDownDelButton.setText(R.string.download);
            mStartButton.setEnabled(false);
            mDownloadIcon.setVisibility(View.VISIBLE);
        }
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
