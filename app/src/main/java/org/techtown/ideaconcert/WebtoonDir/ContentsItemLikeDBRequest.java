package org.techtown.ideaconcert.WebtoonDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ContentsItemLikeDBRequest extends StringRequest {
    private Map<String, String> parameters;

    public ContentsItemLikeDBRequest(String URL, Response.Listener<String> listener, int contents_item_pk, int user_pk) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("contents_item_pk", String.valueOf(contents_item_pk));
        parameters.put("user_pk", String.valueOf(user_pk));
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}