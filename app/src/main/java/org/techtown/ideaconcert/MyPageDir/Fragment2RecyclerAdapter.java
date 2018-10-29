package org.techtown.ideaconcert.MyPageDir;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class Fragment2RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Fragment2RecyclerItem> items = new ArrayList<>();

    public static class Fragment2Holder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView date_text, contents_name_text;
        Button alarm;

        Fragment2Holder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.my_page_fragment2_thumbnail);
            date_text = view.findViewById(R.id.my_page_fragment2_date);
            contents_name_text = view.findViewById(R.id.my_page_fragment2_contents_name);
            alarm = view.findViewById(R.id.my_page_fragment2_alarm);
            alarm.setTag("unchecked");
        }
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

        // fragment1Holder.thumbnail.setImageBitmap(); // 효과적인 thumbnail 설정 방법 알아오기
        fragment2Holder.date_text.setText(item.getDate());
        fragment2Holder.contents_name_text.setText(item.getContents_name());
        fragment2Holder.alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 관심 작품 목록 데이터베이스에서 불러온 내용을 기반으로 알림 설정 여부를 체크, 해제 해야 하지만 다음에 ... 일단 보이는 기능만
                if (fragment2Holder.alarm.getTag().equals("unchecked")) {
                    fragment2Holder.alarm.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.alarm_2));
                    fragment2Holder.alarm.setTag("checked");
                }
                else {
                    fragment2Holder.alarm.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.alarm_1));
                    fragment2Holder.alarm.setTag("unchecked");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected void addItem(Bitmap thumbnail, String date, String contents_name, int contents_pk, int alarm) {
        Fragment2RecyclerItem item = new Fragment2RecyclerItem();
        item.setThumbnail(thumbnail);
        item.setDate(date);
        item.setContents_name(contents_name);
        item.setContents_pk(contents_pk);
        item.setAlarm(alarm);
        items.add(item);
    }
}