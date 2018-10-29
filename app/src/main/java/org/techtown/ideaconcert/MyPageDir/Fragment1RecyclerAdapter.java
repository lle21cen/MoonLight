package org.techtown.ideaconcert.MyPageDir;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class Fragment1RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Fragment1RecyclerItem> items = new ArrayList<>();

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
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_page1_recycler_item, parent, false);
            return new Fragment1RecyclerAdapter.Fragment1Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Fragment1RecyclerAdapter.Fragment1Holder fragment1Holder = (Fragment1RecyclerAdapter.Fragment1Holder) holder;
            final Fragment1RecyclerItem item = items.get(position);

            // fragment1Holder.thumbnail.setImageBitmap(); // 효과적인 thumbnail 설정 방법 알아오기
            fragment1Holder.date_text.setText(item.getDate());
            fragment1Holder.contents_name_text.setText(item.getContents_name());
            fragment1Holder.contents_num.setText(item.getContents_pk());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        protected void addItem(Bitmap thumbnail, String date, String contents_name, String contents_pk) {
            Fragment1RecyclerItem item = new Fragment1RecyclerItem();
            item.setThumbnail(thumbnail);
            item.setDate(date);
            item.setContents_name(contents_name);
            item.setContents_pk(contents_pk);
            items.add(item);
        }

    public ArrayList<Fragment1RecyclerItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<Fragment1RecyclerItem> items) {
        this.items = items;
    }
}
