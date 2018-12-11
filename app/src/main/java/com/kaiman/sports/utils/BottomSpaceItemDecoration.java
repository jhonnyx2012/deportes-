package com.kaiman.sports.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class BottomSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;

    public BottomSpaceItemDecoration(int space) {
        this.mSpace = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = mSpace;
            //outRect.top = mSpace;
        }
    }
}