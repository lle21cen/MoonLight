package org.techtown.ideaconcert.ManageMyWorksDir;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.techtown.ideaconcert.MyPageDir.Fragment1RecentViewWorks;
import org.techtown.ideaconcert.MyPageDir.Fragment2LikeWorks;
import org.techtown.ideaconcert.MyPageDir.Fragment3LikeAuthor;
import org.techtown.ideaconcert.MyPageDir.Fragment4NoticeList;

public class ManageTabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public ManageTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        switch (position) {
            case 0:
                return new Fragment1ProfitManagement();
            case 1:
                return new Fragment1ProfitManagement();
            case 2:
                return new Fragment1ProfitManagement();
            default:
                return new Fragment1ProfitManagement();
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
