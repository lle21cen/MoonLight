package org.techtown.ideaconcert.LoginDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private Map<String, String> parameters;
    final static private String URL = "http://lle21cen.cafe24.com/Login.php";

    public LoginRequest(String email, String pw, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("user_pw", pw);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
