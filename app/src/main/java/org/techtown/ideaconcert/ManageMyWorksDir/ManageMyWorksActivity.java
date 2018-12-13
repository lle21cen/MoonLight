package org.techtown.ideaconcert.ManageMyWorksDir;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

public class ManageMyWorksActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_works);

        Button backBtn =  findViewById(R.id.manage_back_btn);
        TextView backText = findViewById(R.id.manage_title_txt);
        backBtn.setOnClickListener(this);
        backText.setOnClickListener(this);

        TabLayout tabLayout = findViewById(R.id.manage_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("수익 관리"));
        tabLayout.addTab(tabLayout.newTab().setText("정산 관리"));
        tabLayout.addTab(tabLayout.newTab().setText("내 작품"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.manage_back_btn :
            case R.id.manage_title_txt :
                setResult(ActivityCodes.MANAGE_MY_WORKS_FAIL);
                finish();
                break;
        }
    }
}
