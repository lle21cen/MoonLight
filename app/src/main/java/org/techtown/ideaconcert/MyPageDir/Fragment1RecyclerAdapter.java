package org.techtown.ideaconcert.MyPageDir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.techtown.ideaconcert.ContentsMainDir.ContentsMainActivity;
import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class Fragment1RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Fragment1RecyclerItem> items = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_page1_recycler_item, parent, false);
        return new Fragment1RecyclerAdapter.Fragment1Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Fragment1RecyclerAdapter.Fragment1Holder fragment1Holder = (Fragment1RecyclerAdapter.Fragment1Holder) holder;
        final Fragment1RecyclerItem item = items.get(position);

        fragment1Holder.date_text.setText(item.getDate());
        fragment1Holder.contents_name_text.setText(item.getContents_name());
        fragment1Holder.contents_num.setText(String.valueOf(item.getContents_num()));

        SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(fragment1Holder.thumbnail, 80, 60);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, item.getThumbnail_url());

        fragment1Holder.continue_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ContentsMainActivity.class);
                intent.putExtra("contents_pk", item.getContents_pk());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected void addItem(String thumbnail, int contents_pk, String date, String contents_name, int contents_num) {
        Fragment1RecyclerItem item = new Fragment1RecyclerItem();
        item.setThumbnail_url(thumbnail);
        item.setDate(date);
        item.setContents_name(contents_name);
        item.setContents_num(contents_num);
        item.setContents_pk(contents_pk);
        items.add(item);
    }

    public ArrayList<Fragment1RecyclerItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<Fragment1RecyclerItem> items) {
        this.items = items;
    }

    public static class Fragment1Holder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView date_text, contents_name_text, contents_num;
        RelativeLayout continue_layout;

        Fragment1Holder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.my_page_fragment1_thumbnail);
            date_text = view.findViewById(R.id.my_page_fragment1_date);
            contents_name_text = view.findViewById(R.id.my_page_fragment1_contents_name);
            contents_num = view.findViewById(R.id.my_page_fragment1_contents_num);
            continue_layout = view.findViewById(R.id.my_page_fragment1_continue_layout);
            SharedPreferences preferences = view.getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
            int role = preferences.getInt("userRole", 2);
//            Toast.makeText(view.getContext(), "" + role, Toast.LENGTH_SHORT).show();
        }
    }
}
