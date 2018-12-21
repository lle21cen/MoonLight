package org.techtown.ideaconcert.CommentDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AccusationRequest extends StringRequest {
    Map<String, String> parameters;

    public AccusationRequest(String url, Response.Listener<String> listener, int comment_pk, String accused_email, String accused_comment, String accuse_email, String accuse_reason) {
        super(Method.POST, url, listener, null);
        parameters = new HashMap<>();
        parameters.put("comment_pk", String.valueOf(comment_pk));
        parameters.put("accused_email", accused_email);
        parameters.put("accused_comment", accused_comment);
        parameters.put("accuse_email", accuse_email);
        parameters.put("accuse_reason", accuse_reason);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
