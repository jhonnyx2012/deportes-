package com.kaiman.sports.main.activities.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.core.presentation.adapter.BaseListAdapter;
import com.core.presentation.adapter.holder.BaseViewHolder;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ItemActivityBinding;
import com.kaiman.sports.main.activities.ActivityViewModel;

/**
 * Created by jhonnybarrios on 3/18/18
 */

public class ActivityAdapter extends BaseListAdapter<ActivityViewModel, ActivityAdapter.ViewHolder> {

    @Override
    protected RecyclerView.ViewHolder createViewHolder(int viewType, View v) {
        return new ViewHolder(v);
    }

    @Override
    protected int getLayoutIdByType(int viewType) {
        return R.layout.item_activity;
    }

    //TODO: llenar los viewholders con la informacion que venga de las actividades
    public class ViewHolder extends BaseViewHolder<ActivityViewModel, ItemActivityBinding> {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
