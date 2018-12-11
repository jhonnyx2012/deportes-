package com.core.presentation.adapter.holder;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.Locale;

public abstract class BaseViewHolder<MODEL,BINDER extends ViewDataBinding> extends RecyclerView.ViewHolder {
    protected final BINDER binder;

    public BaseViewHolder(View itemView) {
        super(itemView);
        binder= DataBindingUtil.getBinding(itemView);
    }

    public String getString(@StringRes int idString) {
        return itemView.getContext().getString(idString);
    }

    public int getColor(int idColor) {
        return getContext().getResources().getColor(idColor);
    }

    public Context getContext() {
        return itemView.getContext();
    }

    public String getString(@StringRes int idString,String fillText) {
        return String.format(Locale.getDefault(),getString(idString),fillText);
    }

    public Drawable getVectorDrawable(int id) {
        return VectorDrawableCompat.create(getContext().getResources(), id, getContext().getTheme());
    }

    public Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    public void bind(int position, MODEL item){}
}