package org.techtown.ideaconcert.ContentsMainDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WorksDBRequest extends StringRequest {
    private Map<String, String> parameters;

    public WorksDBRequest(String URL, Response.Listener<String> listener, int contents_pk, int user_pk) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("contents_pk", String.valueOf(contents_pk));
        parameters.put("user_pk", String.valueOf(user_pk));
    }

    public WorksDBRequest(String URL, Response.Listener<String> listener, int contents_pk) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("contents_pk", String.valueOf(contents_pk));
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}