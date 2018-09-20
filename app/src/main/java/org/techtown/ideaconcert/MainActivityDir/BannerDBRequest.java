package org.techtown.ideaconcert.MainActivityDir;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BannerDBRequest extends StringRequest {
    private Map<String, String> parameters;
    static final String getBannerInfoURL = "http://lle21cen.cafe24.com/GetBannerInfo.php";

    public BannerDBRequest(Response.Listener<String> listener) {
        super(Method.POST, getBannerInfoURL, listener, null);
        parameters = new HashMap<>();
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}