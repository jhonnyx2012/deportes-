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
import com.kaiman.sports.databinding.ItemMyWorkshopBinding;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public class MyWorkshopAdapter extends BaseListAdapter<WorkshopViewModel, MyWorkshopAdapter.ViewHolder> {
    @Override
    protected RecyclerView.ViewHolder createViewHolder(int viewType, View v) {
        return new ViewHolder(v);
    }

    @Override
    protected int getLayoutIdByType(int viewType) {
        return R.layout.item_my_workshop;
    }

    class ViewHolder extends BaseViewHolder<WorkshopViewModel, ItemMyWorkshopBinding> {
        ViewHolder(View itemView) {super(itemView);}

        @Override
        public void bind(int position, WorkshopViewModel item) {
            binder.textInstructor.setText(getString(R.string.instructor_name, item.instructorName));
            binder.textEnclosure.setText(getString(R.string.enclosure_s, item.enclosure));
            if (item.schedules != null && item.schedules.isEmpty()) {
                binder.textSchedules.setVisibility(View.VISIBLE);
                binder.textSchedules.setText(getString(R.string.time_interval, item.schedules));
            } else {
                binder.textSchedules.setVisibility(View.GONE);
            }
            binder.textTitle.setText(item.title);
            if (item.photoUrl != null) {
                Glide.with(getContext())
                        .load(item.photoUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(binder.imageInstructorPhoto);

            } else if (item.instructorName != null && !item.instructorName.isEmpty()) {
                TextDrawable icon = TextDrawable.builder().buildRound(item.instructorName.charAt(0)+"", Color.GRAY);
                binder.imageInstructorPhoto.setImageDrawable(icon);
            }
            //TODO: implementar click de boton enviar comunicación
        }
    }
}