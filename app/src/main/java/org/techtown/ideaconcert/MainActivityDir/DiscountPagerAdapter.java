package org.techtown.ideaconcert.MainActivityDir;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

import java.net.URL;
import java.util.ArrayList;

public class DiscountPagerAdapter extends PagerAdapter {

    LayoutInflater inflater;
    private ArrayList<DiscountContentsItem> items;

    public DiscountPagerAdapter(LayoutInflater inflater, ArrayList<DiscountContentsItem> items) {
        this.inflater = inflater;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.main_discount_contetns_item, null);
        final ImageView thumbnail_view, title_img_view;
        final TextView title_view, star_rating_view, writer_name_view, painter_name_view, view_count_view, summary_view;

        thumbnail_view = view.findViewById(R.id.discount_item_img);
        title_img_view = view.findViewById(R.id.discount_item_title_img); // 신작이냐 아니냐 구분
        title_view = view.findViewById(R.id.discount_item_title_txt);
        star_rating_view = view.findViewById(R.id.discount_item_star_rating);
        writer_name_view = view.findViewById(R.id.discount_item_writer);
        painter_name_view = view.findViewById(R.id.discount_item_painter);
        view_count_view = view.findViewById(R.id.discount_item_view_count);
        summary_view = view.findViewById(R.id.discount_item_summary);

        DiscountContentsItem item = items.get(position);
        thumbnail_view.setImageBitmap(item.getBitmap());
        title_view.setText(item.getTitle());
        star_rating_view.setText(""+item.getStar_rating());
        writer_name_view.setText(item.getWriter());
        painter_name_view.setText(item.getPainter());
        view_count_view.setText(""+item.getView_count());
        summary_view.setText(item.getSummary());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        try {
            container.removeView((View) object);
        } catch (Exception e) {
            Log.e("destroy item", e.getMessage());
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}