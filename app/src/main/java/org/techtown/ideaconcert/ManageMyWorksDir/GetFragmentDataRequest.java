package org.techtown.ideaconcert.ManageMyWorksDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetFragmentDataRequest extends StringRequest {
    Map<String, String> parameters;

    public GetFragmentDataRequest(String url, Response.Listener<String> listener, int user_pk, int year, int month) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_pk", String.valueOf(user_pk));
        parameters.put("year", String.valueOf(year));
        parameters.put("month", String.valueOf(month));
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
