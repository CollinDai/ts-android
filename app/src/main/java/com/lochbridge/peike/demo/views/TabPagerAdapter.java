package com.lochbridge.peike.demo.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lochbridge.peike.demo.fragment.HotMovieFragment;
import com.lochbridge.peike.demo.fragment.LocalMovieFragment;

/**
 * Created by PDai on 10/16/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            default:
            case 0: return "HOT";
            case 1: return "LOCAL";
        }
    }


    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            HotMovieFragment fragment = new HotMovieFragment();
            return fragment;
        } else {
            return new LocalMovieFragment();
        }
    }
}
