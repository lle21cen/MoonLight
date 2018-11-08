package org.techtown.ideaconcert.CommentDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentInsertLikeDataRequest extends StringRequest {
    private Map<String, String> parameters;

    public CommentInsertLikeDataRequest(String URL, Response.Listener<String> listener, int comment_pk, int user_pk) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("comment_pk", String.valueOf(comment_pk));
        parameters.put("user_pk", String.valueOf(user_pk));
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}