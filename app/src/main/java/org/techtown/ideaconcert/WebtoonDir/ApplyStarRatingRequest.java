package org.techtown.ideaconcert.WebtoonDir;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ApplyStarRatingRequest extends StringRequest {
    Map<String, String> parameters;

    public ApplyStarRatingRequest(String url, Response.Listener<String> listener, int contents_pk, int user_pk, float rating) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("contents_pk", String.valueOf(user_pk));
        parameters.put("user_pk", String.valueOf(user_pk));
        parameters.put("rating", String.valueOf(rating));
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
