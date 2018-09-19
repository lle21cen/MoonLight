package org.techtown.ideaconcert.MainActivityDir;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

// 카테고리 콘텐츠 리스너 -> 오류 상태. 사용 불가
public class CategoryContentsListener implements Response.Listener<String> {
    final int CONTENTS_WIDTH = 200, CONTENTS_HEIGHT = 250; // 카테고리  컨텐츠의 가로, 세로 길이
    LinearLayout contentsLayout;
    Context context;

    public CategoryContentsListener(LinearLayout layout, Context context) {
        contentsLayout = layout;
        this.context = context;
    }

    @Override
    public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");

                if (exist) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(CONTENTS_WIDTH, CONTENTS_HEIGHT);
                    lp.setMargins(10, 10, 10, 10);
                    int num_category_contents_data = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_category_contents_data; i++) {
                        // 데이터베이스에 들어있는 콘텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            URL url = new URL(temp.getString("url"));

                            GetBitmapImageFromURL getBitmapImageFromURL = new GetBitmapImageFromURL(url);
                            getBitmapImageFromURL.start();
                            getBitmapImageFromURL.join();
                            Bitmap bitmap = getBitmapImageFromURL.getBitmap();

                            ImageView contentsImg = new ImageView(context);
                            contentsImg.setImageBitmap(bitmap);

                            contentsImg.setLayoutParams(lp);
                            contentsLayout.addView(contentsImg);
//                            Log.e("size", "" + bitmap.getHeight() + ", " + bitmap.getWidth());
                        } catch (Exception e) {
                            Log.e("setBitmap error", e.getMessage());
                        }
                    }
                } else {
                    Log.e("No Banner", "표시 할 배너가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("contentsListener", e.getMessage());
            }
    }
}
