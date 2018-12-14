package org.techtown.ideaconcert.ManageMyWorksDir;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.UserInformation;

public class Fragment3MyContents extends Fragment {

    View view;
    RecyclerView worksList;
    Fragment3MyContentsAdapter adapter;

    private final String GetAuthorContentsURL = "http://lle21cen.cafe24.com/GetAuthorContents.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myworks_fragment3_myworks, container, false);
        worksList = view.findViewById(R.id.myworks_recycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        worksList.setLayoutManager(manager);

        adapter = new Fragment3MyContentsAdapter();

        UserInformation userInformation = (UserInformation) getActivity().getApplication();
        GetFragmentDataRequest request = new GetFragmentDataRequest(GetAuthorContentsURL, myContentsListener, userInformation.getUser_pk());
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        return view;
    }

    private Response.Listener<String> myContentsListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                boolean exist = jsonObject.getBoolean("exist");
                if (exist) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    int num_result = jsonObject.getInt("num_result");
                    for (int i = 0; i < num_result; i++) {
                        JSONObject temp = jsonArray.getJSONObject(i);

                        String thumbnail_url = ActivityCodes.DATABASE_IP + temp.getString("url");
                        String contents_name = temp.getString("contents_name");
                        String writer_name = temp.getString("writer_name");
                        String painter_name = temp.getString("painter_name");
                        double star_rating = temp.getDouble("star_rating");
                        int movie = temp.getInt("movie");

                        adapter.addItem(thumbnail_url, contents_name, painter_name, movie, star_rating);
                    }
                    worksList.setAdapter(adapter);
                }
            } catch (JSONException je) {
                    Log.e("내작품리스너", je.getMessage());
            }
        }
    };
}
