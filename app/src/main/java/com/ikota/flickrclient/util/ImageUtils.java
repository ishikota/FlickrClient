package com.ikota.flickrclient.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;


public class ImageUtils {

    public static Drawable tintImage(Context context, Drawable src, int res_color) {
        int tint = context.getResources().getColor(res_color);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        src.setColorFilter(tint, mode);
        return src;
    }
}
