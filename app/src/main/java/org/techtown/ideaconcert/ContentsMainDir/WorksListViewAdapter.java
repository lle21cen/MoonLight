package org.techtown.ideaconcert.ContentsMainDir;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class WorksListViewAdapter extends BaseAdapter {

    private ArrayList<WorksListViewItem> worksListViewItems;
    private Context context;

    private FragmentActivity activity;
    //    private final String InsertCashDataURL = "http://lle21cen.cafe24.com/InsertCashData.php";
    private final String InsertCashDataURL = ActivityCodes.DATABASE_IP + "/platform/InsertCashData";
    private SharedPreferences preferences;
    private Handler startWebtonActHandler;

    public WorksListViewAdapter(Context context, FragmentActivity activity, Handler startWebtonActHandler) {
        this.context = context;
        worksListViewItems = new ArrayList<>();
        this.activity = activity;
        this.startWebtonActHandler = startWebtonActHandler;
    }

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
        TextView cashView = convertView.findViewById(R.id.contents_main_item_cash);

        final RelativeLayout cashLayoutView = convertView.findViewById(R.id.contents_main_cash_layout);
        final RelativeLayout downloadLayout = convertView.findViewById(R.id.contents_main_download_layout); // 두 레이아웃 중 택1

        preferences = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        final int cash = preferences.getInt("cash", 0);
        final WorksListViewItem listViewItem = worksListViewItems.get(position);

        if (worksListViewItems.get(position).getPurchased() == 0) {
            cashLayoutView.setVisibility(View.VISIBLE);
            downloadLayout.setVisibility(View.INVISIBLE);
        } else {
            cashLayoutView.setVisibility(View.INVISIBLE);
            downloadLayout.setVisibility(View.VISIBLE);
        }

        cashLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listViewItem.getCash() < cash) {
                    TwoButtonDialog dialog = new TwoButtonDialog(context, cash, listViewItem.getCash(), listViewItem.getContentsItemPk(), position);
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
                    if (listViewItem.getPurchased() != 0) {
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.contents_main_container, new Fragment2Movie(movie_url), "movie").commit();
                        cashLayoutView.setVisibility(View.INVISIBLE);
                        downloadLayout.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(context, R.string.contents_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return convertView;
    }

    public void addItem(int contents_pk, int contents_num, String title, String thumbnail_url, String watch, double star_rating, int comments, String movie_url, int cash, int purchased) {
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
        item.setPurchased(purchased);
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

        private int cash, price, contents_item_pk, position;

        public TwoButtonDialog(@NonNull Context context, int cash, int price, int contents_item_pk, int position) {
            super(context);
            this.cash = cash;
            this.price = price;
            this.contents_item_pk = contents_item_pk;
            this.position = position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
            setContentView(R.layout.two_button_dialog_layout);

            final TextView contentsView = findViewById(R.id.two_button_contents);
            contentsView.setText("작품을 구매하시겠습니까?\n보유캐시 : " + cash + " 가격 : " + price + "\n구매 후 남는 캐시 : " + (cash - price));
            Button firstBtn = findViewById(R.id.two_button_first);
            Button secondBtn = findViewById(R.id.two_button_second);
            firstBtn.setText("취소");
            secondBtn.setText("구매 후 보기");

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

                    InsertCashDataListener listener = new InsertCashDataListener(price, position);
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
        private int position;

        InsertCashDataListener(int price, int position) {
            this.price = price;
            this.position = position;
        }

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    Toast.makeText(context, "작품을 구매했습니다.", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor cashEditor = preferences.edit();
                    int cash = preferences.getInt("cash", 0);
                    cashEditor.putInt("cash", cash - price);
                    cashEditor.apply();

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    message.setData(bundle);
                    startWebtonActHandler.sendMessage(message);
                } else {
                    Log.e("구매에러", jsonObject.getString("errmsg"));
                }
            } catch (JSONException je) {
                Log.e("캐시삽입리스너", je.getMessage());
            }
        }
    }

}
