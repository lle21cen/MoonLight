package org.techtown.ideaconcert.CommentDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentInsertLikeDataRequest extends StringRequest {
    private Map<String, String> parameters;

    /*
    tag = 1 : 댓글 테이블에 정보 저장
    tag = 2 : 답글 테이블에 정보 저장
     */
    public CommentInsertLikeDataRequest(String URL, Response.Listener<String> listener, int comment_pk, int user_pk, int tag) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        if (tag == 1)
            parameters.put("comment_pk", String.valueOf(comment_pk));
        else if (tag == 2)
            parameters.put("reply_pk", String.valueOf(comment_pk));
        parameters.put("user_pk", String.valueOf(user_pk));
        parameters.put("tag", String.valueOf(tag));
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}