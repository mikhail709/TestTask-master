package com.test.testtask.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.test.testtask.ui.fragments.GridFragment;

import java.util.List;

public class ViewPagerAdapter  extends FragmentStatePagerAdapter {

    private List<GridFragment> fragments;

    public ViewPagerAdapter(FragmentManager fragmentManager, List<GridFragment> fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
