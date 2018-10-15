package org.techtown.ideaconcert.MainActivityDir;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class DiscountContentsRecyclerAdatper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DiscountContentsItem> items = new ArrayList<>();

    public static class DiscountViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail_view, title_img_view;
        TextView title_view, star_rating_view, writer_name_view, painter_name_view, view_count_view, summary_view;

        DiscountViewHolder(View view) {
            super(view);
            thumbnail_view = view.findViewById(R.id.discount_item_img);
            title_img_view = view.findViewById(R.id.discount_item_title_img); // 신작이냐 아니냐 구분
            title_view = view.findViewById(R.id.discount_item_title_txt);
            star_rating_view = view.findViewById(R.id.discount_item_star_rating);
            writer_name_view = view.findViewById(R.id.discount_item_writer);
            painter_name_view = view.findViewById(R.id.discount_item_painter);
            view_count_view = view.findViewById(R.id.discount_item_view_count);
            summary_view = view.findViewById(R.id.discount_item_summary);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_discount_contetns_item, parent, false);
        return new DiscountContentsRecyclerAdatper.DiscountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DiscountContentsRecyclerAdatper.DiscountViewHolder discountViewHolder = (DiscountContentsRecyclerAdatper.DiscountViewHolder) holder;
        final DiscountContentsItem item = items.get(position);

        discountViewHolder.thumbnail_view.setImageBitmap(item.getBitmap());
        discountViewHolder.title_view.setText(item.getTitle());
        discountViewHolder.star_rating_view.setText(""+item.getStar_rating());
        discountViewHolder.writer_name_view.setText(item.getWriter());
        discountViewHolder.painter_name_view.setText(item.getPainter());
        discountViewHolder.summary_view.setText(item.getSummary());

        String viewCountText;
        int viewCount = item.getView_count();
        if (viewCount > 1000) {
            viewCount /= 1000;
            viewCountText = viewCount + "k";
        } else {
            viewCountText = viewCount + "";
        }
        discountViewHolder.view_count_view.setText(viewCountText);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Toast.makeText(context, "position=" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void addItem(int contents_pk, Bitmap thumbnail, String title, String writer, String painter, String summary, int view_count, double star_rating) {
        DiscountContentsItem item = new DiscountContentsItem();
        item.setBitmap(thumbnail);
        item.setTitle(title);
        item.setWriter(writer);
        item.setPainter(painter);
        item.setSummary(summary);
        item.setView_count(view_count);
        item.setStar_rating(star_rating);
        items.add(item);
    }
}