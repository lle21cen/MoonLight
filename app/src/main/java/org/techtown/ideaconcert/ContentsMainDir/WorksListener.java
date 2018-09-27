package org.techtown.ideaconcert.ContentsMainDir;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.MainActivityDir.GetBitmapImageFromURL;
import org.techtown.ideaconcert.ShowProgressDialog;
import org.w3c.dom.Text;

import java.net.URL;

// 카테고리 콘텐츠 리스너 -> 오류 상태. 사용 불가
public class WorksListener implements Response.Listener<String> {

    private String title, watch_num, rating, comments_num;
    private WorksListViewAdapter adapter;
    private ListView listView;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWatch_num() {
        return watch_num;
    }

    public void setWatch_num(String watch_num) {
        this.watch_num = watch_num;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComments_num() {
        return comments_num;
    }

    public void setComments_num(String comments_num) {
        this.comments_num = comments_num;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    private URL url;

    public WorksListener(WorksListViewAdapter adapter, ListView listView) {
        this.adapter = adapter;
        this.listView = listView;
    }
    @Override
    public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");

                if (exist) {
                    int num_category_contents_data = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_category_contents_data; i++) {
                        // 데이터베이스에 들어있는 콘텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            title = temp.getString("name");
                            url = new URL(temp.getString("url"));
                            watch_num = temp.getString("watch");
                            rating = temp.getString("grade");
                            comments_num = temp.getString("comments_num");

                            GetBitmapImageFromURL getBitmapImageFromURL = new GetBitmapImageFromURL(url);
                            getBitmapImageFromURL.start();
                            getBitmapImageFromURL.join();
                            Bitmap bitmap = getBitmapImageFromURL.getBitmap();

                            adapter.addItem(title, bitmap, watch_num, rating, comments_num);
                            Log.e("workslistener", "count="+adapter.getCount());
                        } catch (Exception e) {
                            Log.e("set item error", e.getMessage());
                        }
                    }
                    listView.setAdapter(adapter);
                } else {
                    Log.e("No Data", "데이터가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("WorksListener", e.getMessage());
            }
    }
}
