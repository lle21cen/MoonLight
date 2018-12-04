package org.techtown.ideaconcert.MainActivityDir;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.techtown.ideaconcert.MyPageDir.TabPagerAdapter;
import org.techtown.ideaconcert.R;

public class ExpandedContentsActivity extends AppCompatActivity implements View.OnClickListener{

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_contents);
        Intent intent = getIntent();
        int category = intent.getIntExtra("category", 1) - 1;

        TabLayout tabLayout = findViewById(R.id.expand_tab);
        tabLayout.addTab(tabLayout.newTab().setText("인기"));
        tabLayout.addTab(tabLayout.newTab().setText("학원"));
        tabLayout.addTab(tabLayout.newTab().setText("무협"));
        tabLayout.addTab(tabLayout.newTab().setText("액션"));
        tabLayout.addTab(tabLayout.newTab().setText("코믹"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.expand_pager);

        ExpandedContentsTabPagerAdapter pagerAdapter = new ExpandedContentsTabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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
        viewPager.setCurrentItem(category);

        Button backBtn = findViewById(R.id.expand_back);
        TextView backText = findViewById(R.id.expand_txt);
        backBtn.setOnClickListener(this);
        backText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expand_back :
            case R.id.expand_txt :
                finish();
                break;
        }
    }
}
