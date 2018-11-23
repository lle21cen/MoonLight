package org.techtown.ideaconcert.ManageMyWorksDir;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.techtown.ideaconcert.R;

public class ManageMyWorksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_works);

        TabLayout tabLayout = findViewById(R.id.manage_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("수익 관리"));
        tabLayout.addTab(tabLayout.newTab().setText("정산 관리"));
        tabLayout.addTab(tabLayout.newTab().setText("내 작품"));

        final ViewPager viewPager = findViewById(R.id.manage_pager);
        ManageTabPagerAdapter pagerAdapter = new ManageTabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
