package org.techtown.ideaconcert.MyPageDir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.DatabaseRequest;
import org.techtown.ideaconcert.R;

public class Fragment2LikeWorks extends Fragment {
    private final String getLikeWorksListURL = ActivityCodes.DATABASE_IP + "/platform/GetContentsLikeData";
    View view;
    RecyclerView recycler;
    Fragment2RecyclerAdapter adapter;
    LinearLayoutManager recyclerViewManager;
    Response.Listener<String> getLikeWorksListListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean exist = jsonObject.getBoolean("exist");
                if (exist) {
                    int num_result = jsonObject.getInt("num_result");
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i = 0; i < num_result; i++) {
                        JSONObject temp = result.getJSONObject(i);
                        int contents_pk = temp.getInt("contents_pk");
                        String date = temp.getString("date");
                        String contents_name = temp.getString("contents_name");
                        int alarm = temp.getInt("alarm");
                        String url = temp.getString("url");

                        adapter.addItem(null, date, contents_name, contents_pk, alarm, url);
                    }
                    recycler.setAdapter(adapter);
                }
            } catch (Exception e) {
                Log.e("like_works_error", e.getMessage());
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        int user_pk = intent.getIntExtra("user_pk", 0);

        view = inflater.inflate(R.layout.fragment_my_page2_like_works, container, false);
        adapter = new Fragment2RecyclerAdapter(user_pk);
        recycler = view.findViewById(R.id.my_page_fragment2_recycler);
        recyclerViewManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(recyclerViewManager);

        if (user_pk != 0) {
            DatabaseRequest databaseRequest = new DatabaseRequest(getLikeWorksListListener, getLikeWorksListURL, user_pk);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(databaseRequest);
        }
        recycler.setAdapter(adapter);
        return view;
    }
}