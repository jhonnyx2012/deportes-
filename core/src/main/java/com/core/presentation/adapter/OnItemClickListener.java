package com.core.presentation.adapter;

/**
 * Created by jhonnybarrios on 10/20/17.
 */

public interface OnItemClickListener<T> {
    void onItemClick(int adapterPosition, T item);
}