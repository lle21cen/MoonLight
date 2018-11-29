package org.techtown.ideaconcert.ContentsMainDir;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SQLiteDir.DBHelper;
import org.techtown.ideaconcert.SQLiteDir.DBNames;
import org.techtown.ideaconcert.WebtoonMovieDir.WebtoonMovieActivity;

import java.util.ArrayList;

public class WorksListViewAdapter extends BaseAdapter {

    private ArrayList<WorksListViewItem> worksListViewItems = new ArrayList<>();
    private Context context;
    private ArrayList<Integer> data;

    public WorksListViewAdapter(int contents_pk, Context context) {
        this.context = context;
        data = getReadContentsNumArray(contents_pk, context);
    }

    @Override
    public int getCount() {
        return worksListViewItems.size();
    }

    @Override
    public Object getItem(int i) {
        return worksListViewItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contents_main_works_list_items, parent, false);
        }
        final RelativeLayout itemLayout = convertView.findViewById(R.id.contents_main_item_layout);
        final ImageView worksImageView = convertView.findViewById(R.id.contents_main_item_image);
        TextView titleView = convertView.findViewById(R.id.contents_main_item_title);
        TextView watchView = convertView.findViewById(R.id.contents_main_item_watch);
        TextView ratingView = convertView.findViewById(R.id.contents_main_star_rating);
        TextView commentsNumView = convertView.findViewById(R.id.contents_main_item_comments_num);
        ImageView watchImageView = convertView.findViewById(R.id.contents_main_watch_img);

        final WorksListViewItem listViewItem = worksListViewItems.get(position);
//        for (int i=0; i<data.size(); i++) {                                                                        // 왜 안돼 으ㅡㄴㅇ란ㅇ라ㅣ 아나진짜장나ㅣㅁ릐ㅏㅁㄴㅇ
//            Log.i("c_num", "data = " + data.get(i) + "item = " + worksListViewItems.get(position).getContentsNum());
//            if (data.get(i) == worksListViewItems.get(position).getContentsNum()) {
//                itemLayout.setBackgroundColor(Color.rgb(192,192,192));
//                break;
//            }
//        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) worksImageView.getLayoutParams();

        SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(worksImageView, params.width / 2, params.height / 2);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listViewItem.getThumbnail_url());

        titleView.setText(listViewItem.getWorksTitle() + " " + listViewItem.getContentsNum() + "화");
        watchView.setText(listViewItem.getWatchNum());
        ratingView.setText("" + listViewItem.getStar_rating());
        commentsNumView.setText("" + listViewItem.getCommentCount());

        watchImageView.setVisibility(View.GONE);
        String movie_url = listViewItem.getMovie_url();
        if (movie_url != null && !movie_url.isEmpty()) {
            watchImageView.setVisibility(View.VISIBLE);
            watchImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.watch_now));
            watchImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), WebtoonMovieActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
        }

        // 위젯에 대한 이벤트 리스너 작성
        return convertView;
    }

    public void addItem(int contents_pk, int contents_num, String title, String thumbnail_url, String watch, double star_rating, int comments, String movie_url) {
        WorksListViewItem item = new WorksListViewItem();
        item.setContentsItemPk(contents_pk);
        item.setContentsNum(contents_num);
        item.setWorksTitle(title);
        item.setThumbnail_url(thumbnail_url);
        item.setWatchNum(watch);
        item.setStar_rating(star_rating);
        item.setCommentCount(comments);
        item.setMovie_url(movie_url);
        worksListViewItems.add(item);
    }

    public ArrayList<WorksListViewItem> getWorksListViewItems() {
        return worksListViewItems;
    }
    private ArrayList<Integer> getReadContentsNumArray(int contents_pk, Context context) {
        DBHelper dbHelper = new DBHelper(context, DBNames.CONTENTS_DB, null, 1);
        return dbHelper.getReadContentsNum(contents_pk);
    }
}
