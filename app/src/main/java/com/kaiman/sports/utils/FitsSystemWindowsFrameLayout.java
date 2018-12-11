package com.kaiman.sports.utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * A FrameLayout which memorizes the window insets and propagates them to the child views as soon as
 * they are added. Use this layout as a fragment container in place of a standard FrameLayout to
 * propagate window insets to attached fragments.
 *
 * @author Christophe Beyls
 *
 */
public class FitsSystemWindowsFrameLayout extends FrameLayout {

    public FitsSystemWindowsFrameLayout(Context context) {
        this(context, null);
    }

    public FitsSystemWindowsFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitsSystemWindowsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++)
            getChildAt(index).dispatchApplyWindowInsets(insets); // let children know about WindowInsets

        return insets;
    }
}