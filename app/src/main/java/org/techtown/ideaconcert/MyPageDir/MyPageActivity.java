package org.techtown.ideaconcert.MyPageDir;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.techtown.ideaconcert.R;

public class MyPageActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        Toolbar toolbar = findViewById(R.id.my_page_toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.my_page_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("최근 본 작품"));
        tabLayout.addTab(tabLayout.newTab().setText("관심 작품"));
        tabLayout.addTab(tabLayout.newTab().setText("관심 작가"));
        tabLayout.addTab(tabLayout.newTab().setText("알림 내역"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager = findViewById(R.id.my_page_pager);
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
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
