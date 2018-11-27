package org.techtown.ideaconcert.ManageMyWorksDir;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
                return new Fragment1ProfitManagementLayout();
            case 1:
                return new Fragment2CalculateAccountManagement();
            case 2:
                return new Fragment1ProfitManagementLayout();
            default:
                return new Fragment1ProfitManagementLayout();
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
