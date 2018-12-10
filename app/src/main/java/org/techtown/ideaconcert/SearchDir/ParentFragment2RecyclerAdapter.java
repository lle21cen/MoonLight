package org.techtown.ideaconcert.SearchDir;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.ContentsMainDir.ContentsMainActivity;
import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class ParentFragment2RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ParentFragment2SearchResultItem> items = new ArrayList<>();
    Context context;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_parent_fragment2_result_item, parent, false);
        return new ParentFragment2RecyclerAdapter.ParentFragment2ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ParentFragment2RecyclerAdapter.ParentFragment2ViewHolder viewHolder = (ParentFragment2RecyclerAdapter.ParentFragment2ViewHolder) holder;
        final ParentFragment2SearchResultItem item = items.get(position);

        SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(viewHolder.thumbnailView, ActivityCodes.SEARCH_RESULT_CONTENTS_WIDTH, ActivityCodes.SEARCH_RESULT_CONTENTS_HEIGHT);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, item.getThumbnail_url());

        viewHolder.contentsNameView.setText("" + item.getContents_name());
        viewHolder.authorNameView.setText(item.getAuthor_name());
        viewHolder.starRatingView.setText(String.valueOf(item.getStar_rating()));

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
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

    public void addItem(int contents_pk, String thumbnail_url, String contents_name, String author_name, double star_rating, int movie) {
        items.add(new ParentFragment2SearchResultItem(contents_pk, thumbnail_url, contents_name, author_name, star_rating, movie));
        Log.e("검색결과정보 in Adapter", contents_pk + " " + thumbnail_url + " " + contents_name + " " + star_rating + " " + movie);
    }

    class ParentFragment2ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView thumbnailView;
        TextView contentsNameView, authorNameView, starRatingView;

        public ParentFragment2ViewHolder(View view) {
            super(view);
            this.view = view;
            thumbnailView = view.findViewById(R.id.search_result_item_thumbnail);
            contentsNameView = view.findViewById(R.id.search_result_item_contents_name);
            authorNameView = view.findViewById(R.id.search_result_item_author_name);
            starRatingView = view.findViewById(R.id.search_result_item_star_rating);
        }
    }

}
