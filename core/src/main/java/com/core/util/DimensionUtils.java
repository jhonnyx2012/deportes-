package com.core.util;

import android.content.Context;

/**
 * Created by jhonnybarrios on 01-09-17.
 */

public class DimensionUtils {
    public static float dpFromPx(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromSp(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().scaledDensity;
    }
}