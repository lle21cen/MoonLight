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
import org.techtown.ideaconcert.R;

public class ParentFragment2SearchResult extends Fragment {

    View view;
    TextView resultNumberView, searchText;
    Button searchButton;
    String keyword;
    RecyclerView resultList;
    ParentFragment2RecyclerAdapter adapter;

    private final String GetContentsByKeywordURL = "http://lle21cen.cafe24.com/GetContentsByKeyword.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_parent_fragment2_result, container, false);
        resultNumberView = view.findViewById(R.id.search_result_number);
        resultList = view.findViewById(R.id.search_result_recycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        resultList.setLayoutManager(manager);

        searchText = getActivity().findViewById(R.id.search_keyword);
        keyword = searchText.getText().toString();
        searchButton = getActivity().findViewById(R.id.search_search_btn);

        adapter = new ParentFragment2RecyclerAdapter();
        GetContentsByKeywordRequest request = new GetContentsByKeywordRequest(GetContentsByKeywordURL, getContentsByKeywordListener, keyword);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        return view;
    }

    Response.Listener<String> getContentsByKeywordListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean exist = jsonObject.getBoolean("exist");
                if (exist) {
                    JSONArray result = jsonObject.getJSONArray("result");
                    int num_result = jsonObject.getInt("num_result");
                    for (int i = 0; i < num_result; i++) {
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            int contents_pk = temp.getInt("contents_pk");
                            String thumbnail_url = temp.getString("url");
                            String contents_name = temp.getString("contents_name");
//                            String writer_name = temp.getString("writer_name");
                            String painter_name = temp.getString("painter_name");
                            double star_rating = temp.getDouble("star_rating");
                            int movie = temp.getInt("movie");

                            adapter.addItem(contents_pk, thumbnail_url, contents_name, painter_name, star_rating, movie);
                            Log.e("검색결과정보", contents_pk + " " + thumbnail_url + " " + contents_name + " " + star_rating + " " + movie);
                        } catch (Exception e) {
                            Log.e("Except in webtoon", e.getMessage());
                        }
                    }
                    resultNumberView.setText(String.valueOf(num_result));
                    resultList.setAdapter(adapter);
                } else {
                    Log.e("No Data", "데이터가 없습니다.");
                }
            } catch (JSONException je) {
                Log.e("키워드로컨텐츠정보받기오류", je.getMessage());
            }
        }
    };
}
