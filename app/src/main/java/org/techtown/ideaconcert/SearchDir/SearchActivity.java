package org.techtown.ideaconcert.SearchDir;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SQLiteDir.DBHelper;
import org.techtown.ideaconcert.SQLiteDir.DBNames;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    ViewPager viewPager;
    Button recentBtn, popBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText searchText = findViewById(R.id.title_search_text);
        searchText.requestFocus();

        recentBtn = findViewById(R.id.search_recent_btn);
        popBtn = findViewById(R.id.search_pop_btn);
        recentBtn.setOnClickListener(this);
        popBtn.setOnClickListener(this);

        viewPager = findViewById(R.id.search_pager);

        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), 2);
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

        Button searchButton = findViewById(R.id.main_title_search_btn);
        searchButton.setOnClickListener(this);

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
            case R.id.main_title_search_btn :

                DBHelper dbHelper = new DBHelper(SearchActivity.this, DBNames.CONTENTS_DB, null, 1);
//                dbHelper.insertRecentSearchData();
        }
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
}
