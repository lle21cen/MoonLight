package org.techtown.ideaconcert.ContentsMainDir;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        ImageView worksImageView = convertView.findViewById(R.id.contents_main_item_image);
        TextView titleView = convertView.findViewById(R.id.contents_main_item_title);
        TextView watchView = convertView.findViewById(R.id.contents_main_item_watch);
        TextView ratingView = convertView.findViewById(R.id.contents_main_item_rating);
        TextView commentsNumView = convertView.findViewById(R.id.contents_main_item_comments_num);

        WorksListViewItem listViewItem = worksListViewItems.get(position);

        worksImageView.setImageBitmap(listViewItem.getWorksBitmap());
        titleView.setText(listViewItem.getWorksTitle());
        watchView.setText(listViewItem.getWatchNum());
        ratingView.setText(listViewItem.getRating());
        commentsNumView.setText(listViewItem.getComments());

        // 위젯에 대한 이벤트 리스너 작성
        return convertView;
    }

    public void addItem(String title, Bitmap icon, String watch, String rating, String comments) {
        WorksListViewItem item = new WorksListViewItem();
        item.setWorksTitle(title);
        item.setWorksBitmap(icon);
        item.setWatchNum(watch);
        item.setRating(rating);
        item.setComments(comments);

        worksListViewItems.add(item);
    }
}
