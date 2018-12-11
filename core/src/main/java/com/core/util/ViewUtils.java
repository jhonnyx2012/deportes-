package com.core.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;

import com.core.R;

/**
 * Created by jhonnybarrios on 10/23/17.
 */

public class ViewUtils {
    public static void setTint(ImageView view, @ColorRes int color) {
        if (view == null) return;
        view.setColorFilter(view.getContext().getResources().getColor(color));
    }

    public static void  showOkSnackbar(View view, @StringRes int stringID) {
        final Snackbar snackbar = Snackbar.make(view, stringID, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static ColorStateList createTintList(int colorPrimary, int colorPrimaryDark) {
        int[][] states = {{android.R.attr.state_enabled}, // enabled
                {-android.R.attr.state_enabled}, // disabled
                {-android.R.attr.state_checked}, // unchecked
                {android.R.attr.state_pressed}  // pressed
        };
        int[] colors = {colorPrimary, Color.GRAY, colorPrimary, colorPrimaryDark};
        return new ColorStateList(states, colors);
    }

    public static Drawable getTintedDrawable(Context context, int idDrawable, int color) {
        Drawable drawable = context.getResources().getDrawable(idDrawable);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable.mutate(), color);
        return drawable;
    }
}