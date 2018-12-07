package org.techtown.ideaconcert.ContentsMainDir;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.MediaController;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.WebtoonMovieDir.CustomVideoView;

import java.util.ArrayList;

public class Fragment2Movie extends Fragment {
    View view;
    MediaController mc;
    ArrayList<WorksListViewItem> items;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contents_main_fragment2_movie, container, false);

        items = ContentsMainActivity.itemList;
        final CustomVideoView video = view.findViewById(R.id.webtoon_movie_video_view);

        String video_url = ActivityCodes.DATABASE_IP + "/resources/platform/movie/360p.mp4";

        Uri uri = Uri.parse(video_url);
        video.setVideoURI(uri);

        final ProgressDialog dialog;
        mc = new MediaController(getActivity());

        dialog = ProgressDialog.show(getActivity(), "잠시만 기다려주세요.", "동영상을 로딩중입니다......", true);

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.i("비디오 로딩", "완료");
                dialog.dismiss();
                ((ViewGroup) mc.getParent()).removeView(mc);
                ((FrameLayout) view.findViewById(R.id.videoViewWrapper)).addView(mc);
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
        return view;
    }
}
