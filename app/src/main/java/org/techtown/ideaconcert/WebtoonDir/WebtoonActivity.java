package org.techtown.ideaconcert.WebtoonDir;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.MainActivityDir.GetBitmapImageFromURL;
import org.techtown.ideaconcert.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class WebtoonActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout headerLayout, footerLayout, webtoonLayout;
    private boolean isLayoutVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webtoon);

        headerLayout = findViewById(R.id.webtoon_header_layout);
        footerLayout = findViewById(R.id.webtoon_footer_layout);
        webtoonLayout = findViewById(R.id.webtoon_layout);

        ArrayList<String> urls = new ArrayList<>(
                Arrays.asList("http://221.153.227.146:9000/moonlight/015.jpg_W_0.jpg",
                        "http://221.153.227.146:9000/moonlight/015.jpg_W_1.jpg",
                        "http://221.153.227.146:9000/moonlight/015.jpg_W_2.jpg",
                        "http://221.153.227.146:9000/moonlight/015.jpg_W_3.jpg"));

        for (int i=0; i<urls.size(); i++) {
            try {
                URL url =  new URL(urls.get(i));
                GetBitmapImageFromURL getBitmapImageFromURL = new GetBitmapImageFromURL(url);
                Bitmap bitmap = getBitmapImageFromURL.getBitmap();

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.webtoon, null, false);
                ImageView webtoon = layout.findViewById(R.id.webtoon_webtoon_img);
                webtoon.setImageBitmap(bitmap);

                layout.removeView(null);
                webtoonLayout.addView(webtoon);

            } catch (Exception e) {
                Log.e("Excep in webtoon", e.getMessage());
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case(MotionEvent.ACTION_UP) :
                if (!isLayoutVisible) {
                    isLayoutVisible = true;
                    headerLayout.setVisibility(View.VISIBLE);
                    footerLayout.setVisibility(View.VISIBLE);
                }
                else {
                    isLayoutVisible = false;
                    headerLayout.setVisibility(View.GONE);
                    footerLayout.setVisibility(View.GONE);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.webtoon_back :
                setResult(ActivityCodes.WEBTOON_SUCCESS);
                finish();
                break;
            case R.id.webtoon_contents_option :

                break;
            case R.id.webtoon_like_btn :
                break;
            case R.id.webtoon_comments_btn :
                break;
            case R.id.webtoon_prev_btn :
                break;
            case R.id.webtoon_next_btn :
                break;
        }
    }
}
