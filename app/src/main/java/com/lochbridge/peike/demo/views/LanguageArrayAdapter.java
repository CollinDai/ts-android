package com.lochbridge.peike.demo.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lochbridge.peike.demo.model.Language;

/**
 * Created by PDai on 11/11/2015.
 *
 * Used for language multiple selection in search dialog
 */
public class LanguageArrayAdapter extends ArrayAdapter<Language> {
    private Language[] mLanguages;
    private Context mContext;
    public LanguageArrayAdapter(Context context, int resource, Language[] objects) {
        super(context, resource, objects);
        this.mLanguages = objects;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mLanguages.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) convertView;
        int flagResId = mLanguages[position].flagResId;
        String lan = mLanguages[position].name;
        view.setCompoundDrawables(ContextCompat.getDrawable(mContext, flagResId), null, null, null);
        view.setText(lan);
        return view;
    }


}
