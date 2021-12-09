package com.java.jinxuyang.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TypeFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<String> titlesList;


    public TypeFragmentPagerAdapter(FragmentManager fm, List<String> titlesList) {
        super(fm);
        this.titlesList = titlesList;
    }

    @Override
    public Fragment getItem(int position) {
        NewsFragment n = NewsFragment.newInstance();
        n.type = titlesList.get(position);
        return n;
    }

    @Override
    public int getCount() {
        return titlesList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlesList.get(position);
    }
}
