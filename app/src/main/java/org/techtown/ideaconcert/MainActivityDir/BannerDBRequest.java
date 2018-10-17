package org.techtown.ideaconcert.MainActivityDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BannerDBRequest extends StringRequest {
    private Map<String, String> parameters;

    public BannerDBRequest(Response.Listener<String> listener, String url, int tag) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("tag", String.valueOf(tag));
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}