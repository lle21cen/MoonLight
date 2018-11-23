package org.techtown.ideaconcert.ManageMyWorksDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetProfitDataRequest extends StringRequest {
    Map<String, String> parameters;

    public GetProfitDataRequest(String url, Response.Listener<String> listener, int user_pk) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_pk", String.valueOf(user_pk));
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
