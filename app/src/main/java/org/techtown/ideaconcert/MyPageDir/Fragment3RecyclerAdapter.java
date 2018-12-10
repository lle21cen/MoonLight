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

public class Fragment3RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Fragment3RecyclerItem> items = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_page3_recycler_item, parent, false);
        return new Fragment3Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Fragment3RecyclerAdapter.Fragment3Holder fragment3Holder = (Fragment3RecyclerAdapter.Fragment3Holder) holder;
        final Fragment3RecyclerItem item = items.get(position);

        // fragment1Holder.thumbnail.setImageBitmap(); // 효과적인 thumbnail 설정 방법 알아오기
        fragment3Holder.contents_name_text.setText(item.getAuthor_name());
        fragment3Holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 관심 작품 목록 데이터베이스에서 불러온 내용을 기반으로 알림 설정 여부를 체크, 해제 해야 하지만 다음에 ... 일단 보이는 기능만
                if (fragment3Holder.like.getTag().equals("unchecked")) {
                    fragment3Holder.like.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.alarm_2));
                    fragment3Holder.like.setTag("checked");
                } else {
                    fragment3Holder.like.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.alarm_1));
                    fragment3Holder.like.setTag("unchecked");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected void addItem(Bitmap thumbnail, String author_name, int contents_pk) {
        Fragment3RecyclerItem item = new Fragment3RecyclerItem();
        item.setThumbnail(thumbnail);
        item.setAuthor_name(author_name);
        item.setContents_pk(contents_pk);
        items.add(item);
    }

    public static class Fragment3Holder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView date_text, contents_name_text;
        Button like;

        Fragment3Holder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.my_page_fragment3_thumbnail);
            contents_name_text = view.findViewById(R.id.my_page_fragment3_contents_name);
            like = view.findViewById(R.id.my_page_fragment3_like);
            like.setTag("unchecked");
        }

    }
}