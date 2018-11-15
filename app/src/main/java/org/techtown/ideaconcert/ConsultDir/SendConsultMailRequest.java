package org.techtown.ideaconcert.ConsultDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SendConsultMailRequest extends StringRequest {
    private Map<String, String> parameters;

    public SendConsultMailRequest(String URL, Response.Listener<String> listener, String category, String title, String content, String email) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("category", category);
        parameters.put("title", title);
        parameters.put("content", content);
        parameters.put("email", email);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
