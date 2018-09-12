package org.techtown.ideaconcert;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FindIDPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_send, btn_cancel;
    EditText email_txt, code_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_idpassword);

        btn_send = findViewById(R.id.btn_send);
        btn_cancel = findViewById(R.id.btn_cancel);

        email_txt = findViewById(R.id.find_email_txt);
        code_txt = findViewById(R.id.find_code_text);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send :
                if (btn_send.getText().toString().equals("Send")) {
                    btn_send.setText("Submit");
                    sendCodeRequest sendCodeRequest = new sendCodeRequest(email_txt.getText().toString(), responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(FindIDPasswordActivity.this);
                    requestQueue.add(sendCodeRequest);
                }
        }
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    // 이메일이 존재하는 경우 : 이메일로 보낸 인증코드와 비교할 코드를 사용자가 입력하도록 유도
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindIDPasswordActivity.this);
                    builder.setMessage("Check your email").setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    code_txt.setText("");
                                    code_txt.setVisibility(View.VISIBLE);
                                }
                            }).create().show();
                } else {
                    // 가입되어 있는 이메일이 아닌경우. 회원가입을 먼저 하도록 유도
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindIDPasswordActivity.this);
                    builder.setMessage("This e-mail is not exists. Sign up first.").setCancelable(false)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(ActivityCodes.FIND_FAIL);
                                    finish();
                                }
                            }).create().show();
                }
            } catch (Exception e) {
                Log.d("dberror", e.getMessage());
            }
        }
    };

    public class sendCodeRequest extends StringRequest {
        private Map<String, String> parameters;
        final static private String URL = "http://lle21cen.cafe24.com/SendMailCode.php";

        public sendCodeRequest(String userEmail, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            parameters = new HashMap<>();
            parameters.put("userEmail", userEmail);
        }

        public Map<String, String> getParams() {
            return parameters;
        }
    }
}
