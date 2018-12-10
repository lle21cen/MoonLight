package org.techtown.ideaconcert.MyPageDir;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.ContentsMainDir.ContentsLikeDBRequest;
import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class Fragment2RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String insertDeleteContentsLikeDataURL = ActivityCodes.DATABASE_IP + "/platform/InsertDeleteContentsLikeData";
    private ArrayList<Fragment2RecyclerItem> items = new ArrayList<>();
    private boolean is_like_clicked;
    private int user_pk;

    public Fragment2RecyclerAdapter(int user_pk) {
        this.user_pk = user_pk;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_page2_recycler_item, parent, false);
        return new Fragment2RecyclerAdapter.Fragment2Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Fragment2RecyclerAdapter.Fragment2Holder fragment2Holder = (Fragment2RecyclerAdapter.Fragment2Holder) holder;
        final Fragment2RecyclerItem item = items.get(position);
        is_like_clicked = true;

        // fragment1Holder.thumbnail.setImageBitmap(); // 효과적인 thumbnail 설정 방법 알아오기
        fragment2Holder.date_text.setText(item.getDate());
        fragment2Holder.contents_name_text.setText(item.getContents_name());
        SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(fragment2Holder.thumbnail, 80, 60);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, item.getUrl());

        fragment2Holder.pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentsLikeDBRequest request;
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                SuccessCheckListener listener = new SuccessCheckListener(view.getContext(), fragment2Holder);
                if (!is_like_clicked) {
//                    fragment2Holder.pick.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.pick_2));
                    request = new ContentsLikeDBRequest(insertDeleteContentsLikeDataURL, listener, item.getContents_pk(), user_pk, 1);
                } else {
//                    fragment2Holder.pick.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.pick_1));
                    request = new ContentsLikeDBRequest(insertDeleteContentsLikeDataURL, listener, item.getContents_pk(), user_pk, 2);
                }
                queue.add(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected void addItem(Bitmap thumbnail, String date, String contents_name, int contents_pk, int alarm, String url) {
        Fragment2RecyclerItem item = new Fragment2RecyclerItem();
        item.setThumbnail(thumbnail);
        item.setDate(date);
        item.setContents_name(contents_name);
        item.setContents_pk(contents_pk);
        item.setAlarm(alarm);
        item.setUrl(url);
        items.add(item);
    }

    public static class Fragment2Holder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView date_text, contents_name_text;
        Button pick;

        Fragment2Holder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.my_page_fragment2_thumbnail);
            date_text = view.findViewById(R.id.my_page_fragment2_date);
            contents_name_text = view.findViewById(R.id.my_page_fragment2_contents_name);
            pick = view.findViewById(R.id.my_page_fragment2_pick);
        }
    }

    public class SuccessCheckListener implements Response.Listener<String> {
        Context context;
        Fragment2Holder fragment2Holder;

        public SuccessCheckListener(Context context, Fragment2Holder fragment2Holder) {
            this.context = context;
            this.fragment2Holder = fragment2Holder;
        }

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    if (is_like_clicked) {
                        is_like_clicked = false;
                        Toast.makeText(context, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                        fragment2Holder.pick.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.pick_1));
                    } else {
                        is_like_clicked = true;
                        Toast.makeText(context, "관심목록에 담겼습니다.", Toast.LENGTH_SHORT).show();
                        fragment2Holder.pick.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.pick_2));
                    }
                } else {
                    Log.e("success check errmsg", jsonObject.getString("errmsg"));
                }
            } catch (Exception e) {
                Log.e("success check error", e.getMessage());
            }

        }
    }
}