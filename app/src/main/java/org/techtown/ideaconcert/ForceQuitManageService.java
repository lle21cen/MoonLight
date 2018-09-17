package org.techtown.ideaconcert;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ForceQuitManageService extends Service {
    // 사용자가 회원가입 화면에서 강제종료하여 데이터베이스에 임시 저장된 사용자 데이터를 삭제하는 Service
    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ForceQuitManageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("Task Force Quit", "Remove user data");
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String errmsg = jsonResponse.getString("errmsg");
                    if (success) {

                    } else {
                        Log.e("delete error", errmsg);
                    }
                } catch (Exception e) {
                    Log.e("dp error for delete", e.getMessage());
                }
            }
        };
        try {
            DeleteRequest deleteRequest = new DeleteRequest(responseListener, userID);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(deleteRequest);
        } catch (Exception e)
        {
            Log.e("Network access error", e.getMessage());
        }
        finally {
            stopSelf();
        }
    }
}