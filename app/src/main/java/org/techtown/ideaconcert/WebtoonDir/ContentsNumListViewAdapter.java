package org.techtown.ideaconcert.WebtoonDir;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class ContentsNumListViewAdapter extends BaseAdapter {

    private ArrayList<ContentsNumListViewItem> contentsNumListViewItems = new ArrayList<>();

    @Override
    public int getCount() {
        return contentsNumListViewItems.size();
    }

    @Override
    public Object getItem(int i) {
        return contentsNumListViewItems.get(i);
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
            convertView = inflater.inflate(R.layout.webtoon_contents_num_item, parent, false);
        }

        TextView contentsNumView = convertView.findViewById(R.id.contents_main_item_title);
        final ContentsNumListViewItem listViewItem = contentsNumListViewItems.get(position);
        contentsNumView.setText("" + listViewItem.getContents_num());

        return convertView;
    }

    public void addItem(int contents_num) {
        ContentsNumListViewItem item = new ContentsNumListViewItem();
        item.setContents_num(contents_num);

        contentsNumListViewItems.add(item);
    }

    public ArrayList<ContentsNumListViewItem> getContentsNumListViewItems() {
        return contentsNumListViewItems;
    }
}
