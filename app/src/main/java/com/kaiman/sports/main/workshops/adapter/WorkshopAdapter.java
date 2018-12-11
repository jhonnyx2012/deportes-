package com.kaiman.sports.main.workshops.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.core.presentation.adapter.BaseListAdapter;
import com.core.presentation.adapter.holder.BaseViewHolder;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ItemWorkshopBinding;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public class WorkshopAdapter extends BaseListAdapter<WorkshopViewModel, WorkshopAdapter.ViewHolder> {
    @Override
    protected RecyclerView.ViewHolder createViewHolder(int viewType, View v) {
        return new ViewHolder(v);
    }

    @Override
    protected int getLayoutIdByType(int viewType) {
        return R.layout.item_workshop;
    }

    class ViewHolder extends BaseViewHolder<WorkshopViewModel, ItemWorkshopBinding> {
        ViewHolder(View itemView) {super(itemView);}

        @Override
        public void bind(int position, WorkshopViewModel item) {
            binder.textAvailability.setText(getString(R.string.n_available_spaces, item.availability));
            binder.textInstructorName.setText(getString(R.string.instructor_name, item.instructorName));
            binder.textName.setText(item.title);
            if (item.photoUrl != null) {
                Glide.with(getContext())
                        .load(item.photoUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(binder.imagePhoto);
            }  else if (item.title != null && !item.title.isEmpty()) {
                TextDrawable icon = TextDrawable.builder().buildRound(item.title.charAt(0)+"", Color.GRAY);
                binder.imagePhoto.setImageDrawable(icon);
            }
        }
    }
}