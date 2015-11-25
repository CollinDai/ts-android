package com.lochbridge.peike.demo.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lochbridge.peike.demo.model.Subtitle;
import com.lochbridge.peike.demo.util.Constants;

/**
 * Created by PDai on 11/25/2015.
 */
public class SubtitleDialogFragment extends DialogFragment {

    public static SubtitleDialogFragment newInstance(Subtitle subtitle) {

        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_SUBTITLE, subtitle);
        SubtitleDialogFragment fragment = new SubtitleDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Subtitle subtitle = getArguments().getParcelable(Constants.ARG_SUBTITLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (subtitle != null) {


        }
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private class SubDetailListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
