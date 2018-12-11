package com.kaiman.sports.main.workshops.view;

import android.view.View;

import com.core.presentation.activity.BaseRequestActivity;
import com.core.presentation.adapter.OnItemClickListener;
import com.core.presentation.contract.BaseView;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ActivityAttendanceBinding;
import com.kaiman.sports.main.workshops.adapter.AttendanceAdapter;
import com.kaiman.sports.main.workshops.contract.Attendance;
import com.kaiman.sports.main.workshops.model.LessonMemberViewModel;
import java.util.List;

public class AttendanceActivity extends BaseRequestActivity<ActivityAttendanceBinding> implements Attendance.View {

    public static final String LESSON_ID = "lesson_id";
    Attendance.Presenter presenter;
    private AttendanceAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideAttendancePresenter();
    }

    @Override
    protected void initView() {
        showUpButton(true);
        setTitle(getString(R.string.attendance));
        presenter.setLessonId(getIntent().getExtras().getString(LESSON_ID));
        presenter.initialize(this);
    }

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return Injection.providePlaceholderHelper(binder.content);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onRetryLoadingClicked() {
        presenter.onRetryClicked();
    }

    @Override
    public void setupList(List<LessonMemberViewModel> items) {
        hideProgress();
        adapter = new AttendanceAdapter();
        adapter.setList(items);
        binder.list.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener<LessonMemberViewModel>() {
            @Override
            public void onItemClick(int adapterPosition, LessonMemberViewModel item) {
                presenter.toggleMemberAttendance(adapterPosition, item);
            }
        });
    }

    @Override
    public void updateMember(int position, LessonMemberViewModel item) {
        adapter.updateItem(item, position);
    }

    @Override
    public void showNoRegisteredMembersPlaceholder() {
        showPlaceholder(R.drawable.ic_sentiment_dissatisfied, R.string.no_members_found_description, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRetryClicked();
            }
        });
    }
}