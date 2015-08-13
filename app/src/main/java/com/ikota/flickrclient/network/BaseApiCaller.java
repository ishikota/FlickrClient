package com.ikota.flickrclient.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kota on 2015/08/11.
 * <p/>
 * Base class of API caller.
 * This class implements GET and POST method using Volley library.
 */
public class BaseApiCaller {

    protected void get(Context context, final String url, final String[] keys, final String[] values, final ApiListener listener) {
        // Request a string response from the provided URL.
        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("GET", String.format("onResponse called (%s) : %s", url, response));
                        if (listener != null) listener.onPostExecute(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("GET", String.format("onErrorResponse called (%s) : %s", url, error));
                if (listener != null) listener.onErrorListener(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                int size;
                if (keys == null || values == null) size = 0;
                else size = Math.min(keys.length, values.length);

                for (int i = 0; i < size; i++) {
                    params.put(keys[i], values[i]);
                }

                return params;
            }
        };

        int custom_timeout_ms = 5000;
        DefaultRetryPolicy policy = new DefaultRetryPolicy(custom_timeout_ms,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        MyRequestQueue.getInstance(context).addToRequestQueue(sr);
    }

    protected void post(Context context, final String url, final String[] keys, final String[] values, final ApiListener listener) {

        // TODO : attach TAG to the request to specify request when you cancel.
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("POST", String.format("onResponse called (%s) : %s", url, response));
                        if (listener != null) listener.onPostExecute(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("POST", String.format("onErrorResponse called (%s) : %s", url, error));
                        if (listener != null) listener.onErrorListener(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                int size;
                if (keys == null || values == null) size = 0;
                else size = Math.min(keys.length, values.length);

                for (int i = 0; i < size; i++) {
                    params.put(keys[i], values[i]);
                }

                return params;
            }
        };

        MyRequestQueue.getInstance(context).addToRequestQueue(sr);
    }

    /**
     * Callback interface of ApiCaller class
     */
    public interface ApiListener {
        void onPostExecute(String response);

        void onErrorListener(VolleyError error);
    }

}
