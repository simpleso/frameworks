package com.andnux.core.adapter.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Shenbin on 17/6/18.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mTitles;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    public FragmentAdapter(FragmentManager fm,List<Fragment> fragments) {
        this(fm,fragments,null);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null){
            return  mTitles.get(position);
        }
        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        return  mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }
}
