package org.techtown.ideaconcert.ContentsMainDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertCashDataRequest extends StringRequest {
    Map<String, String> parameters;

    public InsertCashDataRequest(String url, Response.Listener<String> listener, int user_pk, int cash, int contents_item_pk) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_pk", String.valueOf(user_pk));
        parameters.put("cash", String.valueOf(cash));
        parameters.put("contents_item_pk", String.valueOf(contents_item_pk));
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
