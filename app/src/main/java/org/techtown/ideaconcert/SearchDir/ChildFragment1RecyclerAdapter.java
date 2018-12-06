package org.techtown.ideaconcert.SearchDir;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SQLiteDir.DBHelper;
import org.techtown.ideaconcert.SQLiteDir.DBNames;
import org.techtown.ideaconcert.SQLiteDir.RecentSearchData;

import java.util.ArrayList;

public class ChildFragment1RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<RecentSearchData> items;
    private Handler cancelHandler, autoSearchHandler;

    public ChildFragment1RecyclerAdapter(ArrayList<RecentSearchData> items, Handler cancelHandler, Handler autoSearchHandler) {
        this.items = items;
        this.cancelHandler = cancelHandler;
        this.autoSearchHandler = autoSearchHandler;
    }


    class Fragment1ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView contentsNameView, dateView;
        ImageView cancelImageView;

        Fragment1ViewHolder(View view) {
            super(view);
            this.view = view;
            contentsNameView = view.findViewById(R.id.search_item_contents_name);
            dateView = view.findViewById(R.id.search_date);
            cancelImageView = view.findViewById(R.id.search_cancel_image);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_child_fragment1_recent_item, parent, false);
        return new ChildFragment1RecyclerAdapter.Fragment1ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ChildFragment1RecyclerAdapter.Fragment1ViewHolder fragment1ViewHolder = (Fragment1ViewHolder) holder;
        RecentSearchData data = items.get(position);
        fragment1ViewHolder.contentsNameView.setText(data.getContents_name());
        fragment1ViewHolder.dateView.setText(data.getSearch_date());
        fragment1ViewHolder.cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DBHelper dbHelper = new DBHelper(context, DBNames.CONTENTS_DB, null, DBNames.DB_VERSION);
                    dbHelper.deleteRecentSearchData(fragment1ViewHolder.contentsNameView.getText().toString());
                    items.remove(position);
                    cancelHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    Log.e("검색기록삭제오류", ""+e.getMessage());
                }
            }
        });

        fragment1ViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("keyword", fragment1ViewHolder.contentsNameView.getText().toString());
                message.setData(bundle);
                autoSearchHandler.sendMessage(message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
