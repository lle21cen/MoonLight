package org.techtown.ideaconcert.SearchDir;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        switch (position) {
            case 0:
                return new ChildFragment1RecentSearch();
            case 1:
                return new ChildFragment2PopSearch();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
