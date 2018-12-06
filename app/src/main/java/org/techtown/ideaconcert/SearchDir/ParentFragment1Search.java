package org.techtown.ideaconcert.SearchDir;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.techtown.ideaconcert.R;

public class ParentFragment1Search extends Fragment implements View.OnClickListener{
    View view;
    ViewPager viewPager;
    Button recentBtn, popBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_parent_fragment1_search, container, false);
        recentBtn = view.findViewById(R.id.search_recent_btn);
        popBtn = view.findViewById(R.id.search_pop_btn);
        recentBtn.setOnClickListener(this);
        popBtn.setOnClickListener(this);

        viewPager = view.findViewById(R.id.search_pager);
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getChildFragmentManager(), 2);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    changeButtonColor(true);
                }else {
                    changeButtonColor(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(0);
        return view;
    }
    protected void changeButtonColor(boolean recentBtnPurple) {
        if (recentBtnPurple) {
            popBtn.setBackgroundColor(Color.rgb(120, 106, 170));
            popBtn.setTextColor(Color.rgb(255, 255, 255));
            recentBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            recentBtn.setTextColor(Color.rgb(120, 106, 170));
        } else {
            recentBtn.setBackgroundColor(Color.rgb(120, 106, 170));
            recentBtn.setTextColor(Color.rgb(255, 255, 255));
            popBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            popBtn.setTextColor(Color.rgb(120, 106, 170));
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_recent_btn :
                viewPager.setCurrentItem(0);
                changeButtonColor(true);
                break;
            case R.id.search_pop_btn :
                viewPager.setCurrentItem(1);
                changeButtonColor(false);
                break;
        }
    }
}
