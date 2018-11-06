package org.techtown.ideaconcert.CommentDir;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    Button bestButton, allButton;
    ViewPager commentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        bestButton = findViewById(R.id.comment_best_btn);
        allButton = findViewById(R.id.comment_all_btn);
        bestButton.setOnClickListener(this);
        allButton.setOnClickListener(this);

        commentPager = findViewById(R.id.comment_view_pager);
        CommentPagerAdapter pagerAdapter = new CommentPagerAdapter(getSupportFragmentManager());
        commentPager.setAdapter(pagerAdapter);
        commentPager.setCurrentItem(0);
        commentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setButtonAndBackgroundColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Intent intent = getIntent();
        int contents_num = intent.getIntExtra("contents_num", 0);

        String item_title = intent.getStringExtra("item_title");
        TextView workNameView = findViewById(R.id.comment_work_name_txt);
        workNameView.setText(item_title + " " + contents_num + "화");
        workNameView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_work_name_txt:
                setResult(ActivityCodes.COMMENT_SUCCESS);
                finish();
                break;
            case R.id.comment_best_btn :
                setButtonAndBackgroundColor(0);
                commentPager.setCurrentItem(0);
                break;
            case R.id.comment_all_btn :
                setButtonAndBackgroundColor(1);
                commentPager.setCurrentItem(1);
                break;
        }
    }

    private void setButtonAndBackgroundColor(int position) {
        if (position == 0) {
            bestButton.setTextColor(Color.rgb(120, 106, 170));
            bestButton.setBackgroundColor(Color.rgb(255, 255, 255));
            allButton.setTextColor(Color.rgb(255, 255, 255));
            allButton.setBackgroundColor(Color.rgb(120, 106, 170));
        } else if (position == 1) {
            allButton.setTextColor(Color.rgb(120, 106, 170));
            allButton.setBackgroundColor(Color.rgb(255, 255, 255));
            bestButton.setTextColor(Color.rgb(255, 255, 255));
            bestButton.setBackgroundColor(Color.rgb(120, 106, 170));
        }
    }
}
