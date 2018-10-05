package org.techtown.ideaconcert;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest {
    private Map<String, String> parameters;
    static final String deleteUserURL = "http://lle21cen.cafe24.com/DeleteUser.php";

    public DeleteRequest(Response.Listener<String> listener, String email) {
        super(Method.POST, deleteUserURL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", email);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
