package org.techtown.ideaconcert.FAQDir;

import android.content.Context;
import android.util.Log;
import android.widget.ExpandableListView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DataListener implements Response.Listener<String> {

    Context context;
    ExpandableAdapter adapter;
    ExpandableListView faqExListView;

    public DataListener(Context context, ExpandableListView faqExListView) {
        this.context = context;
        this.faqExListView = faqExListView;
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
            JSONArray result = jsonResponse.getJSONArray("result");
            boolean exist = jsonResponse.getBoolean("exist");
            if (exist) {
                int num_result = jsonResponse.getInt("num_result");

                ArrayList<String> groupItems = new ArrayList<>();
                HashMap<String, ArrayList<String>> childItems = new HashMap<>();

                ArrayList<String> childContents;
                for (int i = 0; i < num_result; i++) {
                    // 데이터베이스에 들어있는 컨텐츠의 수만큼 for문을 돌려 layout에 image추가
                    try {
                        childContents = new ArrayList<>();

                        JSONObject temp = result.getJSONObject(i);
                        int board_pk = temp.getInt("board_pk");
                        String title = temp.getString("title");
                        String content = temp.getString("content");

                        groupItems.add(title);
                        childContents.add(content);
                        childItems.put(title, childContents);

                    } catch (Exception e) {
                        Log.e("FAQ데이터리스너", e.getMessage ());
                    }
                }

                adapter = new ExpandableAdapter(context, groupItems, childItems);
                faqExListView.setAdapter(adapter);
            } else {
                Log.e("No Data", "데이터가 없습니다.");
            }
        } catch (Exception e) {
            Log.e("FAQ데이터리스너", e.getMessage());
        }

    }
}
