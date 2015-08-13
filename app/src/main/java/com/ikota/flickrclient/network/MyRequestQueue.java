package com.ikota.flickrclient.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by kota on 2014/10/05.
 * This class holds singleton Volley.RequestQue instance.
 * You should access request que through this class,
 * EX.)
 * good : RequestQue requestQue = MyRequestQue.getInstance(mContext).getRequestQue()
 * bad  : RequestQue requestQue = Volley.newRequestQueue(mContext);
 * <p/>
 * -- How to use --
 * RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
 * MySingleton.getInstance(this).addToRequestQueue(stringRequest);
 */
public class MyRequestQueue {
    private static MyRequestQueue mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private MyRequestQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized MyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
