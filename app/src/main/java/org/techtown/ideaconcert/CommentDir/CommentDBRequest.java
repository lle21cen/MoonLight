package org.techtown.ideaconcert.CommentDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentDBRequest extends StringRequest {
    private Map<String, String> parameters;

    public CommentDBRequest(String URL, Response.Listener<String> listener, int item_pk) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("item_pk", String.valueOf(item_pk));
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}