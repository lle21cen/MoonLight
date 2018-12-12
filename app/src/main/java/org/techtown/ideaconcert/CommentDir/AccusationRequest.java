package org.techtown.ideaconcert.CommentDir;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AccusationRequest extends StringRequest {
    Map<String, String> parameters;


    public AccusationRequest(String url, Response.Listener<String> listener, String email, String reason) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("reason", reason);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
