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
import com.kaiman.sports.databinding.ItemLessonMemberBinding;
import com.kaiman.sports.main.workshops.model.LessonMemberViewModel;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public class AttendanceAdapter extends BaseListAdapter<LessonMemberViewModel, AttendanceAdapter.ViewHolder> {
    @Override
    protected RecyclerView.ViewHolder createViewHolder(int viewType, View v) {
        return new ViewHolder(v);
    }

    @Override
    protected int getLayoutIdByType(int viewType) {
        return R.layout.item_lesson_member;
    }

    class ViewHolder extends BaseViewHolder<LessonMemberViewModel, ItemLessonMemberBinding> {
        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(int position, LessonMemberViewModel item) {
            binder.checkBox.setChecked(item.attend);
            binder.textName.setText(item.memberName);
            if (item.memberImage != null && !item.memberImage.isEmpty()) {
                Glide.with(getContext())
                        .load(item.memberImage)
                        .apply(new RequestOptions().circleCrop())
                        .into(binder.imagePhoto);
            } else if (item.memberName != null && !item.memberName.isEmpty()) {
                TextDrawable icon = TextDrawable.builder().buildRound(item.memberName.charAt(0)+"", Color.GRAY);
                binder.imagePhoto.setImageDrawable(icon);
            }
        }
    }
}