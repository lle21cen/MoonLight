package org.techtown.ideaconcert.MyPageDir;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        switch (position) {
            case 0:
                return new Fragment1RecentViewWorks();
            case 1:
                return new Fragment2LikeWorks();
            case 2:
                return new Fragment3LikeAuthor();
            case 3:
                return new Fragment4NoticeList();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
