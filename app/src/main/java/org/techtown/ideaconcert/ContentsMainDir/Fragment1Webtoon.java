package org.techtown.ideaconcert.ContentsMainDir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SQLiteDir.DBHelper;
import org.techtown.ideaconcert.SQLiteDir.DBNames;
import org.techtown.ideaconcert.WebtoonDir.WebtoonActivity;

import java.util.ArrayList;

public class Fragment1Webtoon extends Fragment implements View.OnClickListener {

    private final String getContentsItemURL = ActivityCodes.DATABASE_IP + "/platform/GetContentsInfoData";
    private final String getContentsLIkeCountURL = ActivityCodes.DATABASE_IP + "/platform/GetContentsLikeCount";
    private final String insertDeleteContentsLikeDataURL = ActivityCodes.DATABASE_IP + "/platform/InsertDeleteContentsLikeData";
//    private final String getContentsItemURL = "http://lle21cen.cafe24.com/GetContentsInfoData.php";
    View view;
    int selected_contents_pk;
    private boolean is_like_clicked = false, is_summary_opened = false;

    private Button first_episode_btn, contents_like_btn, summary_btn, follow_up_btn;
    private TextView totalText, readingText, cashText, titlebar_title, info_title, info_writer, info_painter,
            info_view_count, contents_like_count, summary_text;

    private Spinner sortSpinner;
    private ImageView thumbnail, back_btn;

    private int user_pk;

    private WorksListViewAdapter adapter;
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
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    String info_img_url = jsonResponse.getString("url");
                    String main_contents_name = jsonResponse.getString("contents_name");
                    String writer_name = jsonResponse.getString("writer_name");
                    String painter_name = jsonResponse.getString("painter_name");
                    int view_count = jsonResponse.getInt("view_count");
                    String summary = jsonResponse.getString("summary");

                    SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(thumbnail, ActivityCodes.CONTENTS_MAIN_INFO_WIDTH, ActivityCodes.CONTENTS_MAIN_INFO_HEIGHT);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, info_img_url);

                    titlebar_title.setText(main_contents_name);
                    info_title.setText(main_contents_name);
                    info_writer.setText(writer_name);
                    info_painter.setText(painter_name);
                    info_view_count.setText("" + view_count);
                    summary_text.setText(summary);
                } else {
                    Log.e("No Data", "데이터가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("works listener", e.getMessage());
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

    public void setAdapter(WorksListViewAdapter adapter) {
        this.adapter = adapter;
        if (adapter != null) {
            totalText.setText(String.valueOf(adapter.getCount()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contents_main_fragment1_webtoon, container, false);

        Intent intent = getActivity().getIntent();
        selected_contents_pk = intent.getIntExtra("contents_pk", 0);

        sortSpinner = getActivity().findViewById(R.id.contents_main_list_sort);

        // TextView 초기화
        totalText = view.findViewById(R.id.contents_main_status_total); // 전체 몇 편
        readingText = view.findViewById(R.id.contents_main_status_reading); // 그 중 몇 편을 열람
        cashText = view.findViewById(R.id.contents_main_status_cash); // 보유 캐시

        // INFO
        info_title = view.findViewById(R.id.contents_main_info_title);
        info_writer = view.findViewById(R.id.contents_main_info_writer);
        info_painter = view.findViewById(R.id.contents_main_info_painter);
        info_view_count = view.findViewById(R.id.contents_main_info_view_count);
        contents_like_count = view.findViewById(R.id.contents_main_info_like_count);

        titlebar_title = view.findViewById(R.id.contents_main_title_bar_title);
        summary_text = view.findViewById(R.id.contents_main_summary);

        titlebar_title.setOnClickListener(this);

        thumbnail = view.findViewById(R.id.contents_main_info_img);
        back_btn = view.findViewById(R.id.contents_main_back);

        back_btn.setOnClickListener(this);

        first_episode_btn = view.findViewById(R.id.contents_main_first_episode); // 첫화보기 버튼
        contents_like_btn = view.findViewById(R.id.contents_main_info_like_btn);
        summary_btn = view.findViewById(R.id.contents_main_open_summary_btn);
        follow_up_btn = view.findViewById(R.id.contents_main_follow_up_button);
        first_episode_btn.setOnClickListener(this);
        contents_like_btn.setOnClickListener(this);
        summary_btn.setOnClickListener(this);
        follow_up_btn.setOnClickListener(this);

        WorksDBRequest worksDBRequest = new WorksDBRequest(getContentsItemURL, getContentsItemListener, selected_contents_pk);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(worksDBRequest);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginData", Context.MODE_PRIVATE);
        user_pk = sharedPreferences.getInt("userPk", 0);
        int cash = sharedPreferences.getInt("cash", 0);
        cashText.setText(String.valueOf(cash));

        if (user_pk == 0) {
            Toast.makeText(getActivity(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        } else {
            ContentsLikeDBRequest contentsItemLikeDBRequest = new ContentsLikeDBRequest(getContentsLIkeCountURL, getContentsLikeCountListener, selected_contents_pk, user_pk, 1);
            requestQueue.add(contentsItemLikeDBRequest);
        }

        int lastViewContentsNum = getLastViewContentsNum();
        follow_up_btn.setText("이어보기 (" + lastViewContentsNum + "화)");

        ArrayList<Integer> data = getReadContentsNumArray();
        int readCount = data.size();
        readingText.setText("" + readCount); // 열람 목록 개수
        return view;
    }

    @Override
    public void onClick(View view) {
        ArrayList<WorksListViewItem> items;
        int position = 0;
        Intent intent;
        switch (view.getId()) {
            case R.id.contents_main_first_episode:
                items = adapter.getWorksListViewItems();
                if (sortSpinner.getSelectedItemPosition() == 0)
                    position = items.size() - 1;
                else
                    position = 0;
                intent = new Intent(getActivity(), WebtoonActivity.class);
                putExtraData(intent, position);
                startActivityForResult(intent, ActivityCodes.WEBTOON_REQUEST);
                break;
            case R.id.contents_main_back:
            case R.id.contents_main_title_bar_title:
                getActivity().finish();
                break;
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
            case R.id.contents_main_open_summary_btn:
                if (is_summary_opened) {
                    is_summary_opened = false;
                    summary_text.setMaxLines(3);
                    summary_text.setEllipsize(TextUtils.TruncateAt.END);
                    summary_btn.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_keyboard_arrow_down_black_24dp));
                } else {
                    is_summary_opened = true;
                    summary_text.setMaxLines(Integer.MAX_VALUE);
                    summary_text.setEllipsize(null);
                    summary_btn.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_keyboard_arrow_up_black_24dp));
                }
                break;
            case R.id.contents_main_follow_up_button:
                int last_view_contents_num = getLastViewContentsNum();
                items = adapter.getWorksListViewItems();
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getContentsNum() == last_view_contents_num) {
                        position = i;
                        break;
                    }
                }
                intent = new Intent(getActivity(), WebtoonActivity.class);
                putExtraData(intent, position);
                startActivityForResult(intent, ActivityCodes.WEBTOON_REQUEST);
                break;
        }
    }

    public void putExtraData(Intent intent, int position) {
        ContentsMainActivity.itemList = adapter.getWorksListViewItems();
        intent.putExtra("position", position);
        intent.putExtra("contents_pk", selected_contents_pk);
    }

    private ArrayList<Integer> getReadContentsNumArray() {
        DBHelper dbHelper = new DBHelper(getActivity(), DBNames.CONTENTS_DB, null, 1);
        return dbHelper.getReadContentsNum(selected_contents_pk);
    }

    private int getLastViewContentsNum() {
        DBHelper dbHelper = new DBHelper(getActivity(), DBNames.CONTENTS_DB, null, 1);
        return dbHelper.getLastViewContentsNum(selected_contents_pk);
    }

}
