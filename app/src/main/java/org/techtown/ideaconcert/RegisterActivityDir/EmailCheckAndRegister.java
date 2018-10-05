package org.techtown.ideaconcert.RegisterActivityDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EmailCheckAndRegister extends StringRequest {
    private Map<String, String> parameters;

    public EmailCheckAndRegister(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public void checkUserEmail(String email) {
        parameters = new HashMap<>();
        parameters.put("email", email);
    }

    public void doRegister(String email, String pw, String name) {
        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("pw", pw);
        parameters.put("name", name);
    }

    public void sendUserCode(String email, String userCode) {
        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("userCode", userCode);
    }
    public Map<String, String> getParams() {
        return parameters;
    }
}
