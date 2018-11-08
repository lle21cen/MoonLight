package org.techtown.ideaconcert.CommentDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentInsertDeleteRequest extends StringRequest {
    private Map<String, String> parameters;

    public CommentInsertDeleteRequest(String URL, Response.Listener<String> listener, int tag, String email, String comment, int contents_item_pk) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("tag", String.valueOf(tag));
        parameters.put("email", email);
        parameters.put("comment", comment);
        parameters.put("contents_item_pk", String.valueOf(contents_item_pk));
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}