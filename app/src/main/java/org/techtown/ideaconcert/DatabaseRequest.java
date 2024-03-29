package org.techtown.ideaconcert;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DatabaseRequest extends StringRequest {

    Map<String, String> parameters;

    public DatabaseRequest(Response.Listener<String> listener, String url) {
        super(Method.POST, url, listener, null);
    }

    public DatabaseRequest(Response.Listener<String> listener, String url, String email) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", email);
    }

    public DatabaseRequest(Response.Listener<String> listener, String url, String email, String name, String login_method) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("name", name);
        parameters.put("login_method", login_method);
    }

    public DatabaseRequest(Response.Listener<String> listener, String URL, int user_pk) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_pk", String.valueOf(user_pk));
    }

    public void setComment_pk(int comment_pk) {
        parameters = new HashMap<>();
        parameters.put("comment_pk", String.valueOf(comment_pk));
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
