package org.techtown.ideaconcert.MainActivityDir;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.ideaconcert.ContentsMainDir.ContentsMainActivity;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class DiscountPagerAdapter extends PagerAdapter {

    LayoutInflater inflater;
    private ArrayList<DiscountContentsItem> items1, items2, items3;

    public DiscountPagerAdapter(LayoutInflater inflater, ArrayList<DiscountContentsItem> items1, ArrayList<DiscountContentsItem> items2, ArrayList<DiscountContentsItem> items3) {
        this.inflater = inflater;
        this.items1 = items1;
        this.items2 = items2;
        this.items3 = items3;
    }

    @Override
    public int getCount() {
        return items1.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View view = inflater.inflate(R.layout.main_discount_contetns_item, null);
        final Context context = view.getContext();

        final ImageView thumbnail_view, title_img_view;
        final TextView title_view, star_rating_view, writer_name_view, painter_name_view, view_count_view, summary_view;
        final RelativeLayout layout1 = view.findViewById(R.id.discount_item_layout1);

        thumbnail_view = view.findViewById(R.id.discount_item_img1);
        title_img_view = view.findViewById(R.id.discount_item_title_img1); // 신작이냐 아니냐 구분
        title_view = view.findViewById(R.id.discount_item_title_txt1);
        star_rating_view = view.findViewById(R.id.discount_item_star_rating1);
        writer_name_view = view.findViewById(R.id.discount_item_writer1);
        painter_name_view = view.findViewById(R.id.discount_item_painter1);
        view_count_view = view.findViewById(R.id.discount_item_view_count1);
        summary_view = view.findViewById(R.id.discount_item_summary1);

        if (items1.size() > position) {
            DiscountContentsItem item = items1.get(position);
            thumbnail_view.setImageBitmap(item.getBitmap());
            title_view.setText(item.getTitle());
            star_rating_view.setText("" + item.getStar_rating());
            writer_name_view.setText(item.getWriter());
            painter_name_view.setText(item.getPainter());
            view_count_view.setText("" + item.getView_count());
            summary_view.setText(item.getSummary());
        } else {
            layout1.setVisibility(View.GONE);
        }


        final ImageView thumbnail_view2, title_img_view2;
        final TextView title_view2, star_rating_view2, writer_name_view2, painter_name_view2, view_count_view2, summary_view2;
        final RelativeLayout layout2 = view.findViewById(R.id.discount_item_layout2);

        thumbnail_view2 = view.findViewById(R.id.discount_item_img2);
        title_img_view2 = view.findViewById(R.id.discount_item_title_img2); // 신작이냐 아니냐 구분
        title_view2 = view.findViewById(R.id.discount_item_title_txt2);
        star_rating_view2 = view.findViewById(R.id.discount_item_star_rating2);
        writer_name_view2 = view.findViewById(R.id.discount_item_writer2);
        painter_name_view2 = view.findViewById(R.id.discount_item_painter2);
        view_count_view2 = view.findViewById(R.id.discount_item_view_count2);
        summary_view2 = view.findViewById(R.id.discount_item_summary2);

        if (items2.size() > position) {
            DiscountContentsItem item2 = items2.get(position);
            thumbnail_view2.setImageBitmap(item2.getBitmap());
            title_view2.setText(item2.getTitle());
            star_rating_view2.setText("" + item2.getStar_rating());
            writer_name_view2.setText(item2.getWriter());
            painter_name_view2.setText(item2.getPainter());
            view_count_view2.setText("" + item2.getView_count());
            summary_view2.setText(item2.getSummary());
        } else {
            layout2.setVisibility(View.GONE);
        }


        final ImageView thumbnail_view3, title_img_view3;
        final TextView title_view3, star_rating_view3, writer_name_view3, painter_name_view3, view_count_view3, summary_view3;
        final RelativeLayout layout3 = view.findViewById(R.id.discount_item_layout3);
        thumbnail_view3 = view.findViewById(R.id.discount_item_img3);
        title_img_view3 = view.findViewById(R.id.discount_item_title_img3); // 신작이냐 아니냐 구분
        title_view3 = view.findViewById(R.id.discount_item_title_txt3);
        star_rating_view3 = view.findViewById(R.id.discount_item_star_rating3);
        writer_name_view3 = view.findViewById(R.id.discount_item_writer3);
        painter_name_view3 = view.findViewById(R.id.discount_item_painter3);
        view_count_view3 = view.findViewById(R.id.discount_item_view_count3);
        summary_view3= view.findViewById(R.id.discount_item_summary3);


        if (items3.size() > position) {
            DiscountContentsItem item3 = items3.get(position);
            thumbnail_view3.setImageBitmap(item3.getBitmap());
            title_view3.setText(item3.getTitle());
            star_rating_view3.setText("" + item3.getStar_rating());
            writer_name_view3.setText(item3.getWriter());
            painter_name_view3.setText(item3.getPainter());
            view_count_view3.setText("" + item3.getView_count());
            summary_view3.setText(item3.getSummary());
        } else {
            layout3.setVisibility(View.GONE);
        }

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContentsMainActivity.class);
                intent.putExtra("contents_pk", items1.get(position).getContents_pk());
                context.startActivity(intent);
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContentsMainActivity.class);
                intent.putExtra("contents_pk", items2.get(position).getContents_pk());
                context.startActivity(intent);
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContentsMainActivity.class);
                intent.putExtra("contents_pk", items3.get(position).getContents_pk());
                context.startActivity(intent);
            }
        });

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