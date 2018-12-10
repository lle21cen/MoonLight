package org.techtown.ideaconcert.WebtoonMovieDir;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.MediaController;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.ContentsMainDir.ContentsMainActivity;
import org.techtown.ideaconcert.ContentsMainDir.WorksListViewAdapter;
import org.techtown.ideaconcert.ContentsMainDir.WorksListViewItem;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class WebtoonMovieActivity extends AppCompatActivity implements View.OnClickListener {

    MediaController mc;

    // ContentsMainActivity 의 adapter를 가져오는 방법이 무엇이 있을까요....
    // itemList를 가져와서 setAdapter를 다시할까 그냥...

    ArrayList<WorksListViewItem> items;
    private ListView listView;
    private WorksListViewAdapter adapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webtoon_movie);
        items = ContentsMainActivity.itemList;
        listView = findViewById(R.id.contents_main_list_works_list);
        adapter = new WorksListViewAdapter(this, this);
        listView.setAdapter(adapter);

        final CustomVideoView video = findViewById(R.id.webtoon_movie_video_view);

        String video_url = ActivityCodes.DATABASE_IP + "/resources/platform/movie/360p.mp4";

        Uri uri = Uri.parse(video_url);
        video.setVideoURI(uri);

        final ProgressDialog dialog;
        mc = new MediaController(WebtoonMovieActivity.this);

        dialog = ProgressDialog.show(this, "잠시만 기다려주세요.", "동영상을 로딩중입니다......", true);

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.i("비디오 로딩", "완료");
                dialog.dismiss();
                ((ViewGroup) mc.getParent()).removeView(mc);
                ((FrameLayout) findViewById(R.id.videoViewWrapper)).addView(mc);
                mc.setAnchorView(video);
                video.setMediaController(mc);
                video.start();
                mc.setVisibility(View.INVISIBLE);
            }
        });

        video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP && mc != null) {
                    if (mc.getVisibility() == View.VISIBLE) {
                        mc.setVisibility(View.INVISIBLE);
                    } else {
                        mc.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mc.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    }
                }
                return true;
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
