package com.ikota.flickrclient.network.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by kota on 2015/03/06.
 * Singleton class which holds Volley's RequestQue and ImageLoader.
 * <p/>
 * Here is an example of performing RequestQue operation using singleton class.
 * <p/>
 * // Get a RequestQueue
 * RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
 * getRequestQueue();
 * // Add a request (in this example, called stringRequest) to your RequestQueue.
 * MySingleton.getInstance(this).addToRequestQueue(stringRequest);
 * <p/>
 * And here is an example of using ImageLoader with this singleton class.
 * mImageLoader = MySingleton.getInstance(this).getImageLoader();
 * mImageLoader.get(IMAGE_URL, ImageLoader.getImageListener(mImageView,
 * R.drawable.def_image, R.drawable.err_image));
 */
public class MySingleton {
    private static MySingleton mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private MySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new LruCacheSample());
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    @SuppressWarnings("unused")
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}
