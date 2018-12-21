package org.techtown.ideaconcert.SearchDir;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

public class ChildFragment2PopSearch extends Fragment {
    private final String GetPopSearchKeywordURL = ActivityCodes.DATABASE_IP + "/platform/GetPopSearchKeyword";
    View view;
    RecyclerView recyclerView;
    ChildFragment2PopSearchAdapter adapter;
    private TextView searchText;
    private Button searchButton;
    private Response.Listener<String> getPopSearchKeywordListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean exist = jsonObject.getBoolean("exist");
                if (exist) {
                    int num_result = jsonObject.getInt("num_result");
                    JSONArray resultArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < num_result; i++) {
                        JSONObject temp = resultArray.getJSONObject(i);
                        int contents_pk = temp.getInt("contents_pk");
                        String contents_name = temp.getString("contents_name");
                        adapter.addItem(contents_pk, contents_name);
                    }
                    recyclerView.setAdapter(adapter);
                }
            } catch (JSONException je) {
                Log.e("인기검색키워드리스너오류", je.getMessage());
            }
        }
    };
    private Handler autoSearchHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String keyword = message.getData().getString("keyword");
            searchText.setText(keyword);
            searchButton.performClick();
            return true;
        }
    });

    //    private final String GetContentsByKeywordURL = ActivityCodes.DATABASE_IP "/GetPopSearchKeyword";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_child_fragment_recycler_view, container, false);
        recyclerView = view.findViewById(R.id.search_recent_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChildFragment2PopSearchAdapter(autoSearchHandler);

        try {
            searchText = getParentFragment().getActivity().findViewById(R.id.search_keyword);
            searchButton = getParentFragment().getActivity().findViewById(R.id.search_search_btn);
        } catch (NullPointerException ne) {
            Log.e("인기검색 버튼 초기화 오류", ne.getMessage());
        }

        GetContentsByKeywordRequest request = new GetContentsByKeywordRequest(GetPopSearchKeywordURL, getPopSearchKeywordListener); // keyword 불필요
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
        return view;
    }

}
