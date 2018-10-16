package org.techtown.ideaconcert.MainActivityDir;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class NewArrivalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 신작보기와 베트스9과 추천작품에서 사용
    private ArrayList<NewArrivalItem> items = new ArrayList<>();

    public static class NewArrivalViewHolder extends RecyclerView.ViewHolder {
        ImageView worksImageView;
        TextView contents_name_view;
        TextView star_rating_view;
        TextView author_name_view;

        NewArrivalViewHolder(View view) {
            super(view);
            worksImageView = view.findViewById(R.id.main_arrival_item_image);
            contents_name_view = view.findViewById(R.id.main_arrival_item_name);
            star_rating_view = view.findViewById(R.id.main_arrival_item_star_rating);
            author_name_view = view.findViewById(R.id.main_arrival_item_author_name);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_new_arrival_item, parent, false);
        return new NewArrivalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NewArrivalViewHolder newArrivalViewHolder = (NewArrivalViewHolder) holder;
        NewArrivalItem item = items.get(position);
        newArrivalViewHolder.worksImageView.setImageBitmap(item.getBitmap());
        newArrivalViewHolder.author_name_view.setText(item.getPainter_name());
        newArrivalViewHolder.contents_name_view.setText(item.getWork_name());
        newArrivalViewHolder.star_rating_view.setText(""+item.getStar_rating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Toast.makeText(context, "position = " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void addItem(int contents_pk, Bitmap bitmap, String item_name, double star_rating, String painter_name) {
        NewArrivalItem item = new NewArrivalItem();
        item.setContents_pk(contents_pk);
        item.setBitmap(bitmap);
        item.setWork_name(item_name);
        item.setPainter_name(painter_name);
        item.setStar_rating(star_rating);
        items.add(item);
    }
}
