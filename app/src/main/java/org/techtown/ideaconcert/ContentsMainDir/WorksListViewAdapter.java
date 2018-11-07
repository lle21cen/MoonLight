package org.techtown.ideaconcert.ContentsMainDir;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;

public class WorksListViewAdapter extends BaseAdapter {

    private ArrayList<WorksListViewItem> worksListViewItems = new ArrayList<>();

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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contents_main_works_list_items, parent, false);
        }

        final ImageView worksImageView = convertView.findViewById(R.id.contents_main_item_image);
        TextView titleView = convertView.findViewById(R.id.contents_main_item_title);
        TextView watchView = convertView.findViewById(R.id.contents_main_item_watch);
        TextView ratingView = convertView.findViewById(R.id.contents_main_star_rating);
        TextView commentsNumView = convertView.findViewById(R.id.contents_main_item_comments_num);

        final WorksListViewItem listViewItem = worksListViewItems.get(position);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)worksImageView.getLayoutParams();

        SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(worksImageView, params.width/2, params.height/2);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listViewItem.getThumbnail_url());

        titleView.setText(listViewItem.getWorksTitle() + " " + listViewItem.getContentsNum() + "화");
        watchView.setText(listViewItem.getWatchNum());
        ratingView.setText(""+listViewItem.getStar_rating());
        commentsNumView.setText("" + listViewItem.getCommentCount());

        String movie_url = listViewItem.getMovie_url();
        if (movie_url != null)
            Toast.makeText(context, "movie_url= " + movie_url, Toast.LENGTH_SHORT).show();

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
}
