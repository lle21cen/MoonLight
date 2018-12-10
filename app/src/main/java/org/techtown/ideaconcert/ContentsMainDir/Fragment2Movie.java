package org.techtown.ideaconcert.ContentsMainDir;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.WebtoonMovieDir.CustomVideoView;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class Fragment2Movie extends Fragment implements View.OnClickListener {
    private final String getContentsItemURL = ActivityCodes.DATABASE_IP + "/platform/GetContentsInfoData";
    private final String getContentsLIkeCountURL = ActivityCodes.DATABASE_IP + "/platform/GetContentsLikeCount";
    private final String insertDeleteContentsLikeDataURL = ActivityCodes.DATABASE_IP + "/platform/InsertDeleteContentsLikeData";
    View view;
    int selected_contents_pk;
    MediaController mc;
    ArrayList<WorksListViewItem> items;
    boolean is_like_clicked;
    int user_pk;
    private String movie_url;
    private TextView info_title, info_writer, info_painter,
            info_view_count, contents_like_count;
    private ImageView thumbnail;
    private Button contents_like_btn;
    private Response.Listener<String> getContentsLikeCountListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean exist = jsonObject.getBoolean("exist");
                if (exist) {
                    contents_like_btn.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.pick_2));
                    is_like_clicked = true;
                }
                int count = jsonObject.getInt("count");
                contents_like_count.setText("" + count);
            } catch (Exception e) {
                Log.e("like count error", e.getMessage());
            }
        }
    };
    private Response.Listener<String> getContentsItemListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    String info_img_url = jsonResponse.getString("url");
                    String main_contents_name = jsonResponse.getString("contents_name");
                    String writer_name = jsonResponse.getString("writer_name");
                    String painter_name = jsonResponse.getString("painter_name");
                    int view_count = jsonResponse.getInt("view_count");

                    SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(thumbnail, ActivityCodes.CONTENTS_MAIN_INFO_WIDTH, ActivityCodes.CONTENTS_MAIN_INFO_HEIGHT);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, info_img_url);

                    info_title.setText(main_contents_name);
                    info_writer.setText(writer_name);
                    info_painter.setText(painter_name);
                    info_view_count.setText("" + view_count);
                } else {
                    Log.e("No Data", "데이터가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("works listener", "" + e.getMessage() + "scp=" + selected_contents_pk);
            }
        }
    };
    private Response.Listener<String> successCheckListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    if (is_like_clicked) {
                        is_like_clicked = false;
                        Toast.makeText(getActivity(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                        contents_like_count.setText(String.valueOf(Integer.parseInt(contents_like_count.getText().toString()) - 1));
                        contents_like_btn.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_border_black_24dp));
                    } else {
                        is_like_clicked = true;
                        Toast.makeText(getActivity(), "관심목록에 담겼습니다.", Toast.LENGTH_SHORT).show();
                        contents_like_count.setText(String.valueOf(Integer.parseInt(contents_like_count.getText().toString()) + 1));
                        contents_like_btn.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.pick_2));
                    }
                } else {
                    Log.e("success check errmsg", jsonObject.getString("errmsg"));
                }
            } catch (Exception e) {
                Log.e("success check error", e.getMessage());
            }
        }
    };

    @SuppressLint("ValidFragment")
    public Fragment2Movie(String movie_url) {
        this.movie_url = ActivityCodes.DATABASE_IP + movie_url;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contents_main_fragment2_movie, container, false);
        Intent intent = getActivity().getIntent();
        selected_contents_pk = intent.getIntExtra("contents_pk", 0);

        // INFO
        info_title = view.findViewById(R.id.contents_main_info_title);
        info_writer = view.findViewById(R.id.contents_main_info_writer);
        info_painter = view.findViewById(R.id.contents_main_info_painter);
        info_view_count = view.findViewById(R.id.contents_main_info_view_count);
        contents_like_count = view.findViewById(R.id.contents_main_info_like_count);
        contents_like_btn = view.findViewById(R.id.contents_main_info_like_btn);
        thumbnail = view.findViewById(R.id.contents_main_info_img);
        contents_like_btn.setOnClickListener(this);

        items = ContentsMainActivity.itemList;
        final CustomVideoView video = view.findViewById(R.id.webtoon_movie_video_view);

        Uri uri = Uri.parse(movie_url);
        video.setVideoURI(uri);

        final ProgressDialog dialog;
        mc = new MediaController(getActivity());
        ((ViewGroup) mc.getParent()).removeView(mc);
        ((FrameLayout) view.findViewById(R.id.videoViewWrapper)).addView(mc);

        dialog = ProgressDialog.show(getActivity(), "잠시만 기다려주세요.", "동영상을 로딩중입니다......", true);

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.i("비디오 로딩", "완료");
                dialog.dismiss();
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

        WorksDBRequest worksDBRequest = new WorksDBRequest(getContentsItemURL, getContentsItemListener, selected_contents_pk);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(worksDBRequest);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginData", Context.MODE_PRIVATE);
        user_pk = sharedPreferences.getInt("userPk", 0);
        if (user_pk == 0) {
            Toast.makeText(getActivity(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        } else {
            ContentsLikeDBRequest contentsItemLikeDBRequest = new ContentsLikeDBRequest(getContentsLIkeCountURL, getContentsLikeCountListener, selected_contents_pk, user_pk, 1);
            requestQueue.add(contentsItemLikeDBRequest);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contents_main_info_like_btn:
                ContentsLikeDBRequest contentsItemLikeDBRequest;
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                if (is_like_clicked) {
                    contentsItemLikeDBRequest = new ContentsLikeDBRequest(insertDeleteContentsLikeDataURL, successCheckListener, selected_contents_pk, user_pk, 2);
                } else {
                    contentsItemLikeDBRequest = new ContentsLikeDBRequest(insertDeleteContentsLikeDataURL, successCheckListener, selected_contents_pk, user_pk, 1);
                }
                requestQueue.add(contentsItemLikeDBRequest);
                break;
        }
    }

}
