package org.techtown.ideaconcert.ContentsMainDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WorksDBRequest extends StringRequest {
    private Map<String, String> parameters;
    static final String getWorksInfoURL = "http://lle21cen.cafe24.com/GetCategoryContents.php";

    public WorksDBRequest(Response.Listener<String> listener) {
        super(Method.POST, getWorksInfoURL, listener, null);
        parameters = new HashMap<>();
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}