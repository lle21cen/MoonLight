package org.techtown.ideaconcert.noticeDir;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NoticeRecyclerViewItem> items = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item, parent, false);
        return new NoticeRecyclerViewAdapter.NoticeRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NoticeRecyclerViewAdapter.NoticeRecyclerViewHolder recyclerViewHolder = (NoticeRecyclerViewAdapter.NoticeRecyclerViewHolder) holder;
        final NoticeRecyclerViewItem item = items.get(position);

        recyclerViewHolder.titleView.setText(item.getTitle());
        recyclerViewHolder.dateView.setText(item.getDate());
        recyclerViewHolder.contentView.setText(item.getContent());

        recyclerViewHolder.openCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewHolder.contentView.getVisibility() == View.VISIBLE) {
                    recyclerViewHolder.contentView.setVisibility(View.GONE);
                    recyclerViewHolder.openCloseButton.setImageResource(R.drawable.open);
                } else {
                    recyclerViewHolder.contentView.setVisibility(View.VISIBLE);
                    recyclerViewHolder.openCloseButton.setImageResource(R.drawable.closing);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(int board_pk, String title, String date, String content, TextView contentView) {
        items.add(new NoticeRecyclerViewItem(board_pk, title, date, content, contentView));
    }

    public static class NoticeRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView openCloseButton;
        TextView titleView, dateView, contentView;

        NoticeRecyclerViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.notice_title);
            dateView = view.findViewById(R.id.notice_date);
            contentView = view.findViewById(R.id.notice_content);
            openCloseButton = view.findViewById(R.id.notice_open_close_image);
        }
    }
}
