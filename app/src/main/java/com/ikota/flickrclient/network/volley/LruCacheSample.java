package com.ikota.flickrclient.network.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Volley toolbox provides a standard cache implementation via the DiskBasedCache.
 * But to use ImageLoader, in-memory LRU bitmap cache is suitable.
 * This cache is in-memory LRU bitmap cache for ImageLoader.
 */
public class LruCacheSample implements ImageCache {

    private LruCache<String, Bitmap> mMemoryCache;

    LruCacheSample() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8; // int cacheSize = 5 * 1024 * 1024;  // 5MB

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mMemoryCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }
}
