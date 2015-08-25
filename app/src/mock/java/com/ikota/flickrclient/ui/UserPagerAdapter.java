package com.ikota.flickrclient.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class UserPagerAdapter extends FragmentStatePagerAdapter{
    public static final int PAGE_NUM = 3;

    public UserPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new PopularListFragment();
            case 1: return new PopularListFragment();
            case 2: return new PopularListFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }
}
