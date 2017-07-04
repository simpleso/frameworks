package com.andnux.core.http.impl;

import android.content.Context;

import com.andnux.core.http.ICalback;
import com.andnux.core.http.IHttpProxy;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shenbin on 17/6/19.
 */

public class VolleyProxy implements IHttpProxy {

    private Context mContext;
    private RequestQueue queue;

    public VolleyProxy(Context context) {
        mContext = context;
        queue = Volley.newRequestQueue(mContext);
    }

    @Override
    public void get(String url, Map<String, Object> paramet, final  ICalback calback) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (calback != null){
                    calback.onSuccess(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (calback != null){
                    calback.onFailed(error.getMessage());
                }
            }
        });
        queue.add(request);
    }

    @Override
    public void post(String url, final Map<String, Object> paramet, final ICalback calback) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (calback != null){
                    calback.onSuccess(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (calback != null){
                    calback.onFailed(error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                for (String s : paramet.keySet()) {
                    map.put(s,paramet.get(s).toString());
                }
                return map;
            }
        };
        queue.add(request);
    }
}
