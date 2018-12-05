package org.techtown.ideaconcert.SearchDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetContentsByKeywordRequest extends StringRequest {

    Map<String, String> parameters;

    public GetContentsByKeywordRequest(String url, Response.Listener<String> listener, String keyword) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("keyword", keyword);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
