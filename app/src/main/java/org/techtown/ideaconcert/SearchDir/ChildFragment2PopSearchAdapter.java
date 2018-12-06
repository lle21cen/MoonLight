package org.techtown.ideaconcert.SearchDir;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class ChildFragment2PopSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ChildFragment2PopSearchItem> items = new ArrayList<>();
    private Handler autoSearchHandler;

    public ChildFragment2PopSearchAdapter(Handler autoSearchHandler) {
        this.autoSearchHandler = autoSearchHandler;
    }

    class PopSearchViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView keywordNumView, keywordView;

        PopSearchViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            keywordNumView = itemView.findViewById(R.id.pop_search_item_keyword_num);
            keywordView = itemView.findViewById(R.id.pop_search_item_keyword);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_child_fragment2_pop_item, parent, false);
        return new PopSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PopSearchViewHolder viewHolder = (PopSearchViewHolder) holder;
        final ChildFragment2PopSearchItem item = items.get(position);

        viewHolder.keywordNumView.setText(String.valueOf(position+1));
        viewHolder.keywordView.setText(item.getKeyword());

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("keyword", viewHolder.keywordView.getText().toString().trim());
                message.setData(bundle);
                autoSearchHandler.sendMessage(message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(int contents_pk, String keyword) {
        items.add(new ChildFragment2PopSearchItem(contents_pk, keyword));
    }
}
