package org.techtown.ideaconcert.ContentsMainDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WorksDBRequest extends StringRequest {
    private Map<String, String> parameters;

    public WorksDBRequest(String URL, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}