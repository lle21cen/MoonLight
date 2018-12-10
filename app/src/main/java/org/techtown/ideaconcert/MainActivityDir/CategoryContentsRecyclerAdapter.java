package org.techtown.ideaconcert.MainActivityDir;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.ContentsMainDir.ContentsMainActivity;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class CategoryContentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CategoryContentsItem> items = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category_contents_item, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        final CategoryContentsItem item = items.get(position);

        if (item.getWork_name().isEmpty()) {
            ContentsRecyclerItemViewAll viewAll = new ContentsRecyclerItemViewAll(categoryViewHolder.context);
            categoryViewHolder.itemInfoLayout.setVisibility(View.INVISIBLE);
            categoryViewHolder.itemLayout.addView(viewAll);
            categoryViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ExpandedContentsActivity.class);
                    intent.putExtra("category", item.getMovie()); // movie 에 카테고리 번호 저장했던 것 사용
                    context.startActivity(intent);
                }
            });
            return;
        }
        SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(categoryViewHolder.worksImageView, ActivityCodes.CONTENTS_WIDTH, ActivityCodes.CONTENTS_HEIGHT);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, item.getThumbnailUrl());

        categoryViewHolder.author_name_view.setText(item.getPainter_name());
        categoryViewHolder.contents_name_view.setText(item.getWork_name());

        String viewCountText;
        int viewCount = item.getView_count();
        if (viewCount > 1000) {
            viewCount /= 1000;
            viewCountText = viewCount + "k";
        } else {
            viewCountText = viewCount + "";
        }
        if (item.getMovie() == 1) {
            categoryViewHolder.movieImageView.setVisibility(View.VISIBLE);
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

    void addItem(String thumbnailUrl, String item_name, String painter_name, int view_count, int contents_pk, int movie) {
        CategoryContentsItem item = new CategoryContentsItem();
        item.setThumbnailUrl(thumbnailUrl);
        item.setWork_name(item_name);
        item.setPainter_name(painter_name);
        item.setView_count(view_count);
        item.setContents_pk(contents_pk);
        item.setMovie(movie);
        items.add(item);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemLayout, itemInfoLayout;
        ImageView worksImageView, movieImageView;
        TextView contents_name_view;
        TextView author_name_view;
        TextView view_count_view;
        Context context;

        CategoryViewHolder(View view) {
            super(view);
            context = view.getContext();
            itemLayout = view.findViewById(R.id.main_category_item_layout);
            itemInfoLayout = view.findViewById(R.id.main_category_item_info_layout);

            worksImageView = view.findViewById(R.id.main_category_item_img);
            movieImageView = view.findViewById(R.id.main_category_movie_img);
            contents_name_view = view.findViewById(R.id.main_category_item_name);
            author_name_view = view.findViewById(R.id.main_category_author_name);
            view_count_view = view.findViewById(R.id.main_category_view_count);
        }
    }
}