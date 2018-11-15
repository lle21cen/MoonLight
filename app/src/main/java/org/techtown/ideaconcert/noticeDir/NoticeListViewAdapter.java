package org.techtown.ideaconcert.noticeDir;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class NoticeListViewAdapter extends BaseAdapter {
    ArrayList<NoticeListViewItem> items = new ArrayList<>();
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.notice_item, viewGroup, false);
        }

        final TextView titleView = view.findViewById(R.id.notice_title);
        final TextView dateView = view.findViewById(R.id.notice_date);
        final TextView contentView = view.findViewById(R.id.notice_content);

        return view;
    }
}
