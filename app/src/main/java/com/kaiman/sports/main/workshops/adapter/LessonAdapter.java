package com.kaiman.sports.main.workshops.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.core.presentation.adapter.BaseListAdapter;
import com.core.presentation.adapter.OnItemClickListener;
import com.core.presentation.adapter.holder.BaseViewHolder;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ItemLessonBinding;
import com.kaiman.sports.main.workshops.model.LessonViewModel;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public class LessonAdapter extends BaseListAdapter<LessonViewModel, LessonAdapter.ViewHolder> {
    private final boolean userIsMonitor;
    private OnItemClickListener<LessonViewModel> onTakeAttendanceListener;

    public LessonAdapter(boolean userIsMonitor) {
        this.userIsMonitor = userIsMonitor;
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(int viewType, View v) {
        return new ViewHolder(v);
    }

    @Override
    protected int getLayoutIdByType(int viewType) {
        return R.layout.item_lesson;
    }

    public void setOnTakeAttendanceListener(OnItemClickListener<LessonViewModel> onTakeAttendanceListener) {
        this.onTakeAttendanceListener = onTakeAttendanceListener;
    }

    class ViewHolder extends BaseViewHolder<LessonViewModel, ItemLessonBinding> {
        ViewHolder(View itemView) {
            super(itemView);
            binder.textTakeAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTakeAttendanceListener.onItemClick(getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
        }

        @Override
        public void bind(int position, LessonViewModel item) {
            binder.textName.setText(item.title);
            binder.textAssistance.setText(getString(R.string.assistance_counter, item.assistancePercent));
            binder.textParticipantsCount.setText(getString(R.string.participants_count, item.participantsCount));
            binder.textTimeInterval.setText(getString(R.string.time_interval, item.timeInterval));
            binder.textDate.setText(item.startDate.toString("dd/MM/yyyy"));

            if (!userIsMonitor) {
                binder.textTakeAttendance.setVisibility(View.GONE);
                binder.textAssistance.setVisibility(View.GONE);
                binder.textParticipantsCount.setVisibility(View.GONE);
            }
        }
    }
}