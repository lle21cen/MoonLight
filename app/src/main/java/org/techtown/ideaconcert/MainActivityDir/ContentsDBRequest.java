package org.techtown.ideaconcert.MainActivityDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ContentsDBRequest extends StringRequest{
    private Map<String, String> parameters;

    public ContentsDBRequest(Response.Listener<String> listener, String URL, int tag) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("tag", String.valueOf(tag));
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}