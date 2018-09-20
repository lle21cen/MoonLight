package org.techtown.ideaconcert.ContentsMainDir;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class WorksListViewAdapter extends BaseAdapter {

    private ArrayList<WorksListViewItem> worksListViewItems;

    public WorksListViewAdapter() {
        worksListViewItems = new ArrayList<>();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contents_main_works_list_items, parent, false);
        }

        ImageView worksImageView = convertView.findViewById(R.id.imageView1);
        TextView titleView = convertView.findViewById(R.id.textView1);
        TextView tempView = convertView.findViewById(R.id.textView2);

        WorksListViewItem listViewItem = worksListViewItems.get(position);

        worksImageView.setImageDrawable(listViewItem.getWorksDrawable());
        titleView.setText(listViewItem.getWorksTitle());
        tempView.setText(listViewItem.getTempStr());

        return convertView;
    }

    public void addItem(Drawable icon, String title, String desc) {
        WorksListViewItem item = new WorksListViewItem();
        item.setWorksDrawable(icon);
        item.setWorksTitle(title);
        item.setTempStr(desc);

        worksListViewItems.add(item);
    }
}
