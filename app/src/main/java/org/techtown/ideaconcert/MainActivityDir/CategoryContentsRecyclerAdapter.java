package org.techtown.ideaconcert.MainActivityDir;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.ContentsMainDir.ContentsMainActivity;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class CategoryContentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CategoryContentsItem> items = new ArrayList<>();

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView worksImageView;
        TextView contents_name_view;
        TextView author_name_view;
        TextView view_count_view;

        CategoryViewHolder(View view) {
            super(view);
            worksImageView = view.findViewById(R.id.main_category_item_img);
            contents_name_view = view.findViewById(R.id.main_category_item_name);
            author_name_view = view.findViewById(R.id.main_category_author_name);
            view_count_view = view.findViewById(R.id.main_category_view_count);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category_contents_item, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        final CategoryContentsItem item = items.get(position);

        categoryViewHolder.worksImageView.setImageBitmap(item.getBitmap());
        categoryViewHolder.author_name_view.setText(item.getPainter_name());
        categoryViewHolder.contents_name_view.setText(item.getWork_name());

        String viewCountText;
        int viewCount = item.getView_count();
        if (viewCount > 1000)
        {
            viewCount /= 1000;
            viewCountText = viewCount+"k";
        }
        else {
            viewCountText = viewCount+"";
        }
        categoryViewHolder.view_count_view.setText(viewCountText);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, ContentsMainActivity.class);
                intent.putExtra("contents_pk", item.getContents_pk());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void addItem(Bitmap bitmap, String item_name, String painter_name, int view_count, int contents_pk) {
        CategoryContentsItem item = new CategoryContentsItem();
        item.setBitmap(bitmap);
        item.setWork_name(item_name);
        item.setPainter_name(painter_name);
        item.setView_count(view_count);
        item.setContents_pk(contents_pk);
        items.add(item);
    }

    void clearItem() {
        items.clear();
    }
}