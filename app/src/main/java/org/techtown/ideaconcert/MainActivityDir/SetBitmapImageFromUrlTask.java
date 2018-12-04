package org.techtown.ideaconcert.MainActivityDir;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.techtown.ideaconcert.ActivityCodes;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

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
        try {
            URL url = new URL(ActivityCodes.DATABASE_IP + urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream in = connection.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options(); // option 사이즈 width, height 모두 0 나옴

            Bitmap src = BitmapFactory.decodeStream(in, new Rect(0, 0, 0, 0), options);

            if (dstHeight == 0) {
                options.inSampleSize = 2;
                options.inJustDecodeBounds = true;
                dstHeight = options.outHeight * dstWidth / options.outWidth;
                Log.i("이미지크기", "out width = " + options.outWidth + " out height = " + options.outHeight + " dstwidth = " + dstWidth + " dstHeight = " + dstHeight);
            }

            if (src != null) {
                image = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, true);
            }

            if (!src.isRecycled()) {
//                src.recycle();
            }

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