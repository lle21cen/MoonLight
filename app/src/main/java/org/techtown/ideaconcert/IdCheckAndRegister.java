package org.techtown.ideaconcert;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IdCheckAndRegister extends StringRequest {
    private Map<String, String> parameters;

    public IdCheckAndRegister(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public void checkUserID(String userID) {
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    public void doRegister(String userID, String userPassword, String userName, String userEmail) {
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userName", userName);
        parameters.put("userEmail", userEmail);
    }

    public void sendUserCode(String userEmail, String userCode) {
        parameters = new HashMap<>();
        parameters.put("userEmail", userEmail);
        parameters.put("userCode", userCode);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
