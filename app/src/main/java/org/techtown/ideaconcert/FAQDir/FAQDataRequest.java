package org.techtown.ideaconcert.FAQDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FAQDataRequest extends StringRequest {
    Map<String, String> parameters;

    public FAQDataRequest(String url, Response.Listener<String> listener, int category) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("category", String.valueOf(category));
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}