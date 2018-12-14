package org.techtown.ideaconcert.ManageMyWorksDir;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.MyPageDir.Fragment3RecyclerItem;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class Fragment3MyContentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Fragment3MyContentsItem> items = new ArrayList<>();
    class MyContentsViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailView, movieIconView;
        TextView contentNameView, authorNameView, starRatingView;


        public MyContentsViewHolder(View itemView) {
            super(itemView);
            thumbnailView = itemView.findViewById(R.id.myworks_thumbnail);
            contentNameView = itemView.findViewById(R.id.myworks_contents_name);
            authorNameView = itemView.findViewById(R.id.myworks_author_name);
            starRatingView = itemView.findViewById(R.id.myworks_star_rating);
            movieIconView = itemView.findViewById(R.id.myworks_movie_icon);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myworks_fragment3_myworks_item, parent, false);
        return new MyContentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Fragment3MyContentsAdapter.MyContentsViewHolder viewHolder = (Fragment3MyContentsAdapter.MyContentsViewHolder) holder;
        Fragment3MyContentsItem item = items.get(position);

        SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(viewHolder.thumbnailView, ActivityCodes.MY_CONTENTS_WIDTH, ActivityCodes.MY_CONTENTS_HEIGHT);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, item.thumbnail_url);

        viewHolder.starRatingView.setText(String.valueOf(item.star_rating));
        viewHolder.authorNameView.setText(item.author_name);
        viewHolder.contentNameView.setText(item.contents_name);
        if (item.getMovie() == 1) {
            viewHolder.movieIconView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(String thumbnail_url, String contents_name, String author_name,int movie, double star_rating) {
        items.add(new Fragment3MyContentsItem(thumbnail_url, contents_name, author_name, movie, star_rating));
    }
}
