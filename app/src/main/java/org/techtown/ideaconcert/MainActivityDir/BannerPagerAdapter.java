package org.techtown.ideaconcert.MainActivityDir;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.ideaconcert.MainActivityDir.GetBitmapImageFromURL;
import org.techtown.ideaconcert.R;

import java.net.URL;
import java.util.ArrayList;

public class BannerPagerAdapter extends PagerAdapter {

    LayoutInflater inflater;
    private int count, width, height;
    private ArrayList<BannerPagerItem> items;

    public BannerPagerAdapter(LayoutInflater inflater, int count, ArrayList<BannerPagerItem> items, int width, int height) {
        this.inflater = inflater;
        this.count = count;
        this.items = items;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getCount() {
        return count;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.banner_layout, null);
        final ImageView bannerImg = view.findViewById(R.id.banner_img);
        final TextView indicator_red_txt = view.findViewById(R.id.banner_indi_red);
        final TextView indicator_white_txt = view.findViewById(R.id.banner_indi_white);

        bannerImg.setAdjustViewBounds(true);

        SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(bannerImg, width, height);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, items.get(position).getUrl());
//            Log.e("size", ""+bitmap.getHeight()+", " +bitmap.getWidth());
        indicator_red_txt.setText("" + (position + 1));
        indicator_white_txt.setText("/" + count);

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
