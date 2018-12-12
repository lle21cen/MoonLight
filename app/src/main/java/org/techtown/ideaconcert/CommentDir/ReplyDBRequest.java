package org.techtown.ideaconcert.CommentDir;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReplyDBRequest extends StringRequest {
    private Map<String, String> parameters;

    public ReplyDBRequest(String URL, Response.Listener<String> listener, int tag, String email, String reply, int comment_pk) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("tag", String.valueOf(tag));
        parameters.put("email", email);
        parameters.put("reply", reply);
        parameters.put("comment_pk", String.valueOf(comment_pk));
    }

    public Map<String, String> getParams() {
        return parameters;
    }

}
