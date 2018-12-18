package org.techtown.ideaconcert.NoticeDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NoticeDataRequest extends StringRequest {

    Map<String, String> parameters;

    public NoticeDataRequest(String url, Response.Listener<String> listener, int category) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("category", String.valueOf(category));
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
