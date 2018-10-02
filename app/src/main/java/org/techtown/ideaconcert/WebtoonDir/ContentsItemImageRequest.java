package org.techtown.ideaconcert.WebtoonDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ContentsItemImageRequest extends StringRequest {
    private Map<String, String> parameters;

    public ContentsItemImageRequest(String URL, Response.Listener<String> listener, int contents_item_pk) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("contents_item_pk", String.valueOf(contents_item_pk));
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}