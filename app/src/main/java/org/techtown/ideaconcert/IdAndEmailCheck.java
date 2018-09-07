package org.techtown.ideaconcert;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IdAndEmailCheck extends StringRequest{
    private Map<String, String> parameters;

    public IdAndEmailCheck(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public void checkUserID(String userID) {
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    public void sendUserEmail(String userEmail) {
        parameters = new HashMap<>();
        parameters.put("userEmail", userEmail);
    }

    public void sendUserCode(String userCode) {
        parameters = new HashMap<>();
        parameters.put("userCode", userCode);
    }
    public Map<String, String> getParams() {
        return parameters;
    }
}
