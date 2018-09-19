package org.techtown.ideaconcert.MainActivityDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ContentsDBRequest extends StringRequest{
    private Map<String, String> parameters;
    final static private String URL = "http://lle21cen.cafe24.com/GetCategoryContents.php";

    public ContentsDBRequest(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}