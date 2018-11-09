package org.techtown.ideaconcert.CommentDir;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.ContentsMainDir.ContentsMainActivity;
import org.techtown.ideaconcert.MainActivityDir.CategoryContentsItem;
import org.techtown.ideaconcert.MainActivityDir.CategoryContentsRecyclerAdapter;
import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class ReplyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CommentListViewItem> items = new ArrayList<>();

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {

        TextView emailView, dateView, commentView, likeNumView, accusationView, likeButton;

        ReplyViewHolder(View view) {
            super(view);
            emailView = view.findViewById(R.id.reply_item_email);
            dateView = view.findViewById(R.id.reply_item_date);
            commentView = view.findViewById(R.id.reply_item_comment);
            likeNumView = view.findViewById(R.id.reply_item_like_num);
            accusationView = view.findViewById(R.id.reply_accusation);
            likeButton = view.findViewById(R.id.reply_like_btn);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_reply_item, parent, false);
        return new ReplyRecyclerViewAdapter.ReplyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ReplyRecyclerViewAdapter.ReplyViewHolder replyViewHolder = (ReplyRecyclerViewAdapter.ReplyViewHolder) holder;
        final CommentListViewItem item = items.get(position);

        SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(replyViewHolder.worksImageView, 100, 140);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, item.getThumbnailUrl());

        replyViewHolder.author_name_view.setText(item.getPainter_name());
        replyViewHolder.contents_name_view.setText(item.getWork_name());

        String viewCountText;
        int viewCount = item.getView_count();
        if (viewCount > 1000) {
            viewCount /= 1000;
            viewCountText = viewCount + "k";
        } else {
            viewCountText = viewCount + "";
        }
        if (item.getMovie()==1) {
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
    void clearItem() {
        items.clear();
    }
}