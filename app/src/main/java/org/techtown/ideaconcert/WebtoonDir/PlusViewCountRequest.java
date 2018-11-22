package org.techtown.ideaconcert.WebtoonDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PlusViewCountRequest extends StringRequest {
    Map<String, String> parameters;

    public PlusViewCountRequest(String url, Response.Listener<String> listener, int contents_item_pk) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("contents_item_pk", String.valueOf(contents_item_pk));
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
