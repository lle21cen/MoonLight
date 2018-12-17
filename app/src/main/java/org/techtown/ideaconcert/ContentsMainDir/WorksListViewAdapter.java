package org.techtown.ideaconcert.ContentsMainDir;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.share.Share;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.UserInformation;

import java.util.ArrayList;

public class WorksListViewAdapter extends BaseAdapter {

    private ArrayList<WorksListViewItem> worksListViewItems;
    private Context context;

    private FragmentActivity activity;
    private final String InsertCashDataURL = "http://lle21cen.cafe24.com/InsertCashData.php";
//    private final String UpdateCashURL = ActivityCodes.DATABASE_IP + "/platform/UpdateCash";
    private SharedPreferences preferences;

    private ImageView itemImageView; // 캐시 이미지 혹은 결제완료 이미지가 들어갈 ImageView
    private TextView cashView;
    private RelativeLayout cashLayoutView;

    public WorksListViewAdapter(Context context, FragmentActivity activity) {
        this.context = context;
        worksListViewItems = new ArrayList<>();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return worksListViewItems.size();
    }

    @Override
    public Object getItem(int i) {
        return worksListViewItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contents_main_works_list_items, parent, false);
        }

        final ImageView worksImageView = convertView.findViewById(R.id.contents_main_item_image);
        TextView titleView = convertView.findViewById(R.id.contents_main_item_title);
        TextView watchView = convertView.findViewById(R.id.contents_main_item_watch);
        TextView ratingView = convertView.findViewById(R.id.contents_main_star_rating);
        TextView commentsNumView = convertView.findViewById(R.id.contents_main_item_comments_num);
        ImageView watchImageView = convertView.findViewById(R.id.contents_main_watch_img);
        cashView = convertView.findViewById(R.id.contents_main_item_cash);
        cashLayoutView = convertView.findViewById(R.id.contents_main_cash_layout);
        itemImageView = convertView.findViewById(R.id.contents_main_cash_img);

        preferences = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        final int cash = preferences.getInt("cash", 0);
        final WorksListViewItem listViewItem = worksListViewItems.get(position);

        cashLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listViewItem.getCash() < cash) {
                    TwoButtonDialog dialog = new TwoButtonDialog(context, cash, listViewItem.getCash(), listViewItem.getContentsItemPk());
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else {
                    MsgDialog dialog = new MsgDialog(context);
                    dialog.show();
                }
            }
        });

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) worksImageView.getLayoutParams();

        SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(worksImageView, params.width / 2, params.height / 2);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listViewItem.getThumbnail_url());

        titleView.setText(listViewItem.getWorksTitle() + " " + listViewItem.getContentsNum() + "화");
        watchView.setText(listViewItem.getWatchNum());
        ratingView.setText("" + listViewItem.getStar_rating());
        commentsNumView.setText("" + listViewItem.getCommentCount());
        cashView.setText("" + listViewItem.getCash());

        watchImageView.setVisibility(View.GONE);
        final String movie_url = listViewItem.getMovie_url();
        if (movie_url != null && !movie_url.isEmpty()) {
            watchImageView.setVisibility(View.VISIBLE);
            watchImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.watch_now));
            watchImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.contents_main_container, new Fragment2Movie(movie_url), "movie").commit();
                }
            });
        }
        return convertView;
    }

    public void addItem(int contents_pk, int contents_num, String title, String thumbnail_url, String watch, double star_rating, int comments, String movie_url, int cash, int isPurchased) {
        WorksListViewItem item = new WorksListViewItem();
        item.setContentsItemPk(contents_pk);
        item.setContentsNum(contents_num);
        item.setWorksTitle(title);
        item.setThumbnail_url(thumbnail_url);
        item.setWatchNum(watch);
        item.setStar_rating(star_rating);
        item.setCommentCount(comments);
        item.setMovie_url(movie_url);
        item.setCash(cash);
        item.setIsPurchased(isPurchased);
        worksListViewItems.add(item);
    }

    public ArrayList<WorksListViewItem> getWorksListViewItems() {
        return worksListViewItems;
    }

    class MsgDialog extends Dialog {
        public MsgDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
            setContentView(R.layout.msg_dialog_layout);

            TextView msgView = findViewById(R.id.msg_dialog_txt);
            msgView.setText("먼저 캐시 충전을 해주세요");
        }
    }

    class TwoButtonDialog extends Dialog {

        private int cash, price, contents_item_pk;

        public TwoButtonDialog(@NonNull Context context, int cash, int price, int contents_item_pk) {
            super(context);
            this.cash = cash;
            this.price = price;
            this.contents_item_pk = contents_item_pk;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
            setContentView(R.layout.two_button_dialog_layout);

            final TextView contentsView = findViewById(R.id.two_button_contents);
            contentsView.setText("작품을 구매하시겠습니까?\n보유캐시 : " + cash + " 가격 : " +price + "\n구매 후 남는 캐시 : " + (cash-price));
            Button firstBtn = findViewById(R.id.two_button_first);
            Button secondBtn = findViewById(R.id.two_button_second);
            firstBtn.setText("취소");
            secondBtn.setText("구매하기");

            firstBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            secondBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int user_pk = preferences.getInt("userPk", 0);
                    if (user_pk == 0) {
                        Log.e("user_pk에러", "" + user_pk);
                    }

                    InsertCashDataListener listener = new InsertCashDataListener(price);
                    InsertCashDataRequest request = new InsertCashDataRequest(InsertCashDataURL, listener, user_pk, -price, contents_item_pk);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(request);

                    dismiss();
                }
            });
        }
    }

    class InsertCashDataListener implements Response.Listener<String> {
        private int price;
        InsertCashDataListener(int price) {
            this.price = price;
        }

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    Toast.makeText(context, "구매에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor cashEditor = preferences.edit();
                    int cash = preferences.getInt("cash", 0);
                    cashEditor.putInt("cash", cash-price);
                    cashEditor.apply();

                    // 코인 모양을 다운 완료 모양으로 바꾸고 자동으로 웹툰 실행
//                    itemImageView.setImageResource(R.drawable.download);
//                    cashLayoutView.setBackgroundColor(Color.rgb(239, 239, 239));
//                    cashView.setVisibility(View.GONE);
                } else {
                    Log.e("구매에러", jsonObject.getString("errmsg"));
                }
            } catch (JSONException je) {
                Log.e("캐시삽입리스너", je.getMessage());
            }
        }
    }
}
