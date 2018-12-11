package com.kaiman.sports.main.workshops.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import com.core.presentation.activity.BaseRequestActivity;
import com.core.presentation.adapter.OnItemClickListener;
import com.core.presentation.contract.BaseView;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ActivityLessonsBinding;
import com.kaiman.sports.main.workshops.adapter.LessonAdapter;
import com.kaiman.sports.main.workshops.contract.Lessons;
import com.kaiman.sports.main.workshops.model.LessonViewModel;
import com.kaiman.sports.utils.BottomSpaceItemDecoration;
import com.kaiman.sports.utils.Utils;
import java.util.List;

public class LessonsActivity extends BaseRequestActivity<ActivityLessonsBinding> implements Lessons.View {

    public static final String WORKSHOP_ID = "workshop_id";
    public static final String WORKSHOP_TITLE = "workshop_title";
    Lessons.Presenter presenter;
    private LessonAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lessons;
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideLessonsPresenter();
    }

    @Override
    protected void initView() {
        showUpButton(true);
        if (getIntent().getExtras() != null) {
            presenter.setWorkshopData(
                    getIntent().getExtras().getString(WORKSHOP_ID),
                    getIntent().getExtras().getString(WORKSHOP_TITLE));
        }
        presenter.initialize(this);
        binder.list.addItemDecoration(new BottomSpaceItemDecoration(Utils.dpToPx(74)));
        binder.fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(LessonFormActivity.KEY_IS_CREATING, true);
                bundle.putString(LessonFormActivity.KEY_WORKSHOP_ID, getIntent().getExtras().getString(WORKSHOP_ID));
                startActivity(LessonFormActivity.class, bundle);
            }
        });

        binder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshContent();
            }
        });
    }

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return Injection.providePlaceholderHelper(binder.content);
    }

    @Override
    public void setupList(List<LessonViewModel> lessonViewModels, boolean userIsMonitor) {
        adapter = new LessonAdapter(userIsMonitor);
        setupList(lessonViewModels);
    }

    @Override
    public void setupList(List<LessonViewModel> items) {
        hideProgress();
        adapter.setList(items);
        binder.list.setAdapter(adapter);
        adapter.setOnTakeAttendanceListener(new OnItemClickListener<LessonViewModel>() {
            @Override
            public void onItemClick(int adapterPosition, LessonViewModel item) {
                openLessonAttendance(item);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener<LessonViewModel>() {
            @Override
            public void onItemClick(int adapterPosition, LessonViewModel item) {
                Bundle bundle = new Bundle();
                bundle.putString(LessonFormActivity.KEY_LESSON_ID, item.id);
                bundle.putString(LessonFormActivity.KEY_WORKSHOP_ID, getIntent().getExtras().getString(WORKSHOP_ID));
                startActivity(LessonFormActivity.class, bundle);
            }
        });
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        binder.swipeRefreshLayout.setRefreshing(false);
    }

    private void openLessonAttendance(LessonViewModel item) {
        Bundle bundle = new Bundle();
        bundle.putString(AttendanceActivity.LESSON_ID, item.id);
        startActivity(AttendanceActivity.class, bundle);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        if(!binder.swipeRefreshLayout.isRefreshing()) {
            super.showProgress();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.refreshContent();
    }

    @Override
    public void onRetryLoadingClicked() {
        presenter.onRetryClicked();
    }

    @Override
    public void updateTitle(String workshopTitle) {
        setTitle(workshopTitle);
    }

    @Override
    public void showNoLessonsPlaceholder() {
        showPlaceholder(R.drawable.ic_sentiment_dissatisfied, R.string.no_lessons_found_description, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRetryClicked();
            }
        });
    }

    @Override
    public void setupBasicUserView() {
        binder.fabCreate.setVisibility(View.GONE);
    }
}