package org.techtown.ideaconcert.MainActivityDir;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetBitmapImageFromURL extends Thread {
    private Bitmap bitmap;
    private URL url;

    public GetBitmapImageFromURL(URL url) {
        this.url = url;
    }

    @Override
    public void run() {
        super.run();
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            Log.e("bannerThread", ""+e.getMessage());
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
