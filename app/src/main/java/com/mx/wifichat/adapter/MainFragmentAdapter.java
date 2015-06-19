package com.mx.wifichat.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.mx.wifichat.MyApplication;

import java.util.List;

/**
 * Created by MX on 2015/6/16.
 * FragmentPagerAdapter that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<Fragment> mFragments;
    private List<String> mFragmentTitles;

    public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        mContext = MyApplication.getInstance();
        mFragments = fragments;
        mFragmentTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
