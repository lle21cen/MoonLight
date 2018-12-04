package org.techtown.ideaconcert.MainActivityDir;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

@SuppressLint("ValidFragment")
public class ExpandedContentsFragment extends Fragment {

    final private String categoryContentsURL = ActivityCodes.DATABASE_IP + "/platform/GetCategoryContents";
    View view;
    GridLayout gridLayout;
    Context context;
    private int category;

    @SuppressLint("ValidFragment")
    public ExpandedContentsFragment(int category) {
        this.category = category;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.expand_contents_fragment, container, false);
        gridLayout = view.findViewById(R.id.expand_grid_layout);
        context = view.getContext();

        ContentsDBRequest request = new ContentsDBRequest(getContentsListener, categoryContentsURL, 0, category);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

        return view;
    }

    Response.Listener<String> getContentsListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    JSONArray result = jsonResponse.getJSONArray("result");
                    int num_result = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_result; i++) {
                        // 데이터베이스에 들어있는 콘텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            int contents_pk = temp.getInt("contents_pk");
                            String url = temp.getString("url");
                            String contents_name = temp.getString("contents_name");
                            String painter_name = temp.getString("painter_name");
//                            String writer_name = temp.getString("writer_name"); // 글 작가 이름 추가?
                            int view_count = temp.getInt("view_count");
                            int movie = temp.getInt("movie");
                            ExpandedContentsItemLayout itemLayout = new ExpandedContentsItemLayout(context, url, contents_name, painter_name, view_count, contents_pk, movie);
                            gridLayout.addView(itemLayout);
                            gridLayout.setUseDefaultMargins(true);
                            gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
                        } catch (Exception e) {
                            Log.e("펼쳐보기컨텐츠정보가져오기", e.getMessage());
                        }
                    }
                } else {
                    Log.e("펼쳐보기컨텐츠정보가져오기", "표시 할 컨텐츠가 없습니다.");
                }

            } catch (JSONException je) {
                Log.e("컨텐츠펼쳐보기에러", je.getMessage());
            } catch (Exception e) {
                Log.e("컨텐츠펼쳐보기에러", e.getMessage());
            }
        }
    };
}
