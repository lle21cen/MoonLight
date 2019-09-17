package org.techtown.ideaconcert.MainActivityDir;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.techtown.ideaconcert.ActivityCodes;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SetBitmapImageFromUrlTask extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    private int dstWidth, dstHeight;

    public SetBitmapImageFromUrlTask(ImageView imageView, int dstWidth, int dstHeight) {
        this.imageView = imageView;
        this.dstWidth = dstWidth;
        this.dstHeight = dstHeight;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlStr = urls[0];
        Bitmap image = null;
        int size = 1;
        try {
            URL url = new URL(ActivityCodes.DATABASE_IP + urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream in = connection.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options(); // option 사이즈 width, height 모두 0 나옴
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, new Rect(0, 0, 0, 0), options);
            int requestWidth = options.outWidth;
            
            if (dstHeight == 0) {
                options.inSampleSize = 2;
            } else {
                while(requestWidth < dstWidth){
                    dstWidth = dstWidth / 2;
                    size = size * 2;
                }
                options.inSampleSize = size;
            }
            
            options.inJustDecodeBounds = false;
            image = BitmapFactory.decodeStream(in, new Rect(0, 0, 0, 0), options);
            connection.disconnect();
        } catch (MalformedURLException e) {
            Log.e("url형식오류", e.getMessage());
        } catch (IOException e) {
            Log.e("ioException", e.getMessage());
        } catch (OutOfMemoryError oom) {
            Log.e("메모리초과오류", "" + oom.getMessage());
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }
}
