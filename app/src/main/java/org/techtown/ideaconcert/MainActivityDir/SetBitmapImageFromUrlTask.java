package org.techtown.ideaconcert.MainActivityDir;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream in = connection.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2;
            Bitmap src = BitmapFactory.decodeStream(in, new Rect(0,0,0,0), options);
            image = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, true);
            connection.disconnect();
        }
        catch (MalformedURLException e) {
            Log.e("malformedException", e.getMessage());
        }
        catch (IOException e) {
            Log.e("ioException", e.getMessage());
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
        Log.e("execute", "success");
    }
}