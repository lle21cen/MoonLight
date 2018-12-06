package org.techtown.ideaconcert.WebtoonMovieDir;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

public class WebtoonMovieActivity extends AppCompatActivity implements View.OnClickListener{

    MediaController mc;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webtoon_movie);

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
