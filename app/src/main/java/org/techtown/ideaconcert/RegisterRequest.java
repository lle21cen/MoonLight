package org.techtown.ideaconcert;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private Map<String, String> parameters;

    public RegisterRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public void doRegister(String userID) {
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
