package com.lochbridge.peike.demo.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lochbridge.peike.demo.fragment.HotMovieFragment;
import com.lochbridge.peike.demo.fragment.LocalMovieFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private Fragment hotMovieFragment;

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
            case 0:
                return "HOT";
            case 1:
                return "LOCAL";
        }
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            hotMovieFragment = new HotMovieFragment();
            return hotMovieFragment;
        } else {
            return new LocalMovieFragment();
        }
    }
}
