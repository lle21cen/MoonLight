package org.techtown.ideaconcert.MainActivityDir;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ExpandedContentsTabPagerAdapter extends FragmentStatePagerAdapter {


    private int tabCount;

    public ExpandedContentsTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        return new ExpandedContentsFragment(position+1);
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
