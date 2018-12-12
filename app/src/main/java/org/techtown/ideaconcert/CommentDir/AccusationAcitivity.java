package org.techtown.ideaconcert.CommentDir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.UserInformation;

public class AccusationAcitivity extends AppCompatActivity implements View.OnClickListener {

    private final String SendAccusationURL = "http://lle21cen.cafe24.com/SendAccusation.php";
    private TextView textCountView;
    private EditText reasonTextView;
    private String user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accusation_acitivity);

        Button backBtn = findViewById(R.id.accusation_back_btn);
        TextView backText = findViewById(R.id.accusation_back_txt);
        backBtn.setOnClickListener(this);
        backText.setOnClickListener(this);

        TextView commentUserEmailView = findViewById(R.id.accusation_email_text);
        TextView commentView = findViewById(R.id.accusation_comment_text);
        Button accustaionSubmitButton = findViewById(R.id.accusation_send_btn);
        accustaionSubmitButton.setOnClickListener(this);

        reasonTextView = findViewById(R.id.accusation_reason_text);
        textCountView = findViewById(R.id.accusation_reason_count);

        Intent intent = getIntent();
        String comment_user_email = intent.getStringExtra("comment_user_email");
        String comment = intent.getStringExtra("comment");

        commentUserEmailView.setText(comment_user_email);
        commentView.setText(comment);

        UserInformation userInformation = (UserInformation) getApplication();
        user_email = userInformation.getUserEmail();


        reasonTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textCountView.setText(count + "/100");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accusation_back_btn:
            case R.id.accusation_back_txt:
                setResult(ActivityCodes.ACCUSATION_SUCCESS);
                finish();
                break;
            case R.id.accusation_send_btn :
                AccusationRequest request = new AccusationRequest(SendAccusationURL, sendAccusationListener, user_email, reasonTextView.getText().toString());
                RequestQueue queue = Volley.newRequestQueue(AccusationAcitivity.this);
                queue.add(request);
                break;
        }
    }

    private Response.Listener<String> sendAccusationListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    Toast.makeText(AccusationAcitivity.this, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("신고접수실패", jsonObject.getString("errmsg"));
                }
            } catch (JSONException je) {
                Log.e("신고접수리스너오류", je.getMessage());
            }
        }
    }
}
