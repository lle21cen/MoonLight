package org.techtown.ideaconcert.MainActivityDir;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.ContentsMainDir.ContentsMainActivity;
import org.techtown.ideaconcert.R;

public class ExpandedContentsItemLayout extends RelativeLayout {

    private String thumbnailUrl;
    private String contents_name, author_name;
    private int view_count, contents_pk, movie;

    public ExpandedContentsItemLayout(Context context, String thumbnailUrl, String contents_name, String author_name, int view_count, int contents_pk, int movie) {
        super(context);
        this.thumbnailUrl = thumbnailUrl;
        this.contents_name = contents_name;
        this.author_name = author_name;
        this.view_count = view_count;
        this.contents_pk = contents_pk;
        this.movie = movie;

        init(context);
    }

    void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main_category_contents_item, this, true);

        ImageView thumbnailView = view.findViewById(R.id.main_category_item_img);
        TextView contentsNameView = view.findViewById(R.id.main_category_item_name);
        ImageView movieImageView = findViewById(R.id.main_category_movie_img);
        TextView authorNameView = findViewById(R.id.main_category_author_name);
        TextView viewCountView = findViewById(R.id.main_category_view_count);

        SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(thumbnailView, ActivityCodes.CONTENTS_WIDTH, ActivityCodes.CONTENTS_HEIGHT);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, thumbnailUrl);

        contentsNameView.setText(contents_name);
        if (movie == 1) {
            movieImageView.setVisibility(VISIBLE);
        }
        authorNameView.setText(author_name);
        viewCountView.setText(""+view_count);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ContentsMainActivity.class);
                intent.putExtra("contents_pk", contents_pk);
                view.getContext().startActivity(intent);
            }
        });
    }
}
