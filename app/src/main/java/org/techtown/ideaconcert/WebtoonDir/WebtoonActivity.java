package org.techtown.ideaconcert.WebtoonDir;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.MainActivityDir.GetBitmapImageFromURL;
import org.techtown.ideaconcert.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class WebtoonActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout headerLayout, footerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webtoon);

        headerLayout = findViewById(R.id.webtoon_header_layout);
        footerLayout = findViewById(R.id.webtoon_footer_layout);

        ArrayList<String> urls = new ArrayList<>(
                Arrays.asList("http://221.153.227.146:9000/moonlight/015.jpg_W_0.jpg",
                        "http://221.153.227.146:9000/moonlight/015.jpg_W_1.jpg",
                        "http://221.153.227.146:9000/moonlight/015.jpg_W_2.jpg",
                        "http://221.153.227.146:9000/moonlight/015.jpg_W_3.jpg"));

        LinearLayout webtoonLayout = findViewById(R.id.main_webtoon_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(10, 10, 10, 10);
        for (int i = 0; i < urls.size(); i++) {
            try {
                URL url = new URL(urls.get(i));
                GetBitmapImageFromURL getBitmapImageFromURL = new GetBitmapImageFromURL(url);
                getBitmapImageFromURL.start();
                getBitmapImageFromURL.join();
                Bitmap bitmap = getBitmapImageFromURL.getBitmap();

                ImageView webtoon = new ImageView(this);
                webtoon.setImageBitmap(bitmap);
                webtoon.setAdjustViewBounds(true); // 이미지의 가로를 화면 전체 크기에 맞춤.
                webtoon.setLayoutParams(lp);

                webtoonLayout.addView(webtoon);
            } catch (Exception e) {
                Log.e("Except in webtoon", e.getMessage());
            }
        }

        Button backBtn = findViewById(R.id.webtoon_back);
        backBtn.setOnClickListener(this);
        webtoonLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.webtoon_back:
                setResult(ActivityCodes.WEBTOON_SUCCESS);
                finish();
                break;
            case R.id.webtoon_contents_option:

                break;
            case R.id.webtoon_like_btn:
                break;
            case R.id.webtoon_comments_btn:
                break;
            case R.id.webtoon_prev_btn:
                break;
            case R.id.webtoon_next_btn:
                break;
            case R.id.main_webtoon_layout:
                if (headerLayout.getVisibility() == View.GONE) {
                    headerLayout.setVisibility(View.VISIBLE);
                    footerLayout.setVisibility(View.VISIBLE);
                } else {
                    headerLayout.setVisibility(View.GONE);
                    footerLayout.setVisibility(View.GONE);
                }
                break;

        }
    }
}
