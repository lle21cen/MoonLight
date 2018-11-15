package org.techtown.ideaconcert.FindPasswordDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IssueTempPasswordRequest extends StringRequest {

    Map<String, String> parameters;

    public IssueTempPasswordRequest(String url, Response.Listener<String> listener, String email, String name) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("name", name);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
