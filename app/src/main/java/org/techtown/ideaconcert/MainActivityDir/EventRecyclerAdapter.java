package org.techtown.ideaconcert.MainActivityDir;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

// 신작보기와 베트스9과 추천작품에서 사용
private ArrayList<BannerItem> items = new ArrayList<>();

public static class TodayEventViewHolder extends RecyclerView.ViewHolder {
    ImageView bannerImageView;

    TodayEventViewHolder(View view) {
        super(view);
        bannerImageView = view.findViewById(R.id.event_banner_image);
    }
}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_event_recycler_item, parent, false);
        return new EventRecyclerAdapter.TodayEventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        EventRecyclerAdapter.TodayEventViewHolder todayEventViewHolder = (EventRecyclerAdapter.TodayEventViewHolder) holder;
        final BannerItem item = items.get(position);
        todayEventViewHolder.bannerImageView.setImageBitmap(item.getBitmap());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Toast.makeText(context, ""+item.getContents_pk(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void addItem(int contents_pk, Bitmap bitmap) {
        BannerItem item = new BannerItem();
        item.setContents_pk(contents_pk);
        item.setBitmap(bitmap);
        items.add(item);
    }
}
