package org.techtown.ideaconcert;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.MainActivityDir.GetBitmapImageFromURL;

import java.net.URL;
import java.util.ArrayList;

public class BannerPagerAdapter extends PagerAdapter {

    LayoutInflater inflater;
    private int count;
    private ArrayList<URL> urls;

    public BannerPagerAdapter(LayoutInflater inflater, int count, ArrayList<URL> urls) {
        this.inflater = inflater;
        this.count = count;
        this.urls = urls;
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
        GetBitmapImageFromURL bannerThread = new GetBitmapImageFromURL(urls.get(position));
        bannerThread.start();
        try {
            bannerThread.join();
            Bitmap bitmap = bannerThread.getBitmap();
            bannerImg.setImageBitmap(bitmap);
//            Log.e("size", ""+bitmap.getHeight()+", " +bitmap.getWidth());
            indicator_red_txt.setText(""+(position+1));
            indicator_white_txt.setText("/"+count);
        }catch (Exception e) {
            Log.e("setBitmap error", e.getMessage());
        }
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
