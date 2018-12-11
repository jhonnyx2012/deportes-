package com.kaiman.sports.main.activities.presenter;

import android.support.annotation.NonNull;
import com.core.presentation.BasePresenter;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.activity.ActivityDataSource;
import com.kaiman.sports.main.activities.Activities;
import com.kaiman.sports.main.activities.ActivityViewModel;
import java.util.List;

/**
 * Created by jhonnybarrios on 3/18/18
 */

public class ActivitiesPresenter extends BasePresenter<Activities.View> implements Activities.Presenter {

    private ActivityDataSource activitiesRepository;

    public ActivitiesPresenter(@NonNull ActivityDataSource activitiesRepository) {
        this.activitiesRepository = activitiesRepository;
    }

    @Override
    public void initialize(Activities.View view) {
        super.initialize(view);
        fetchActivities();
    }

    private void fetchActivities() {
        view.showProgress();
        addSubscription(activitiesRepository.getActivities().subscribeWith(new DisposableObserver<List<ActivityViewModel>>(view) {
            @Override
            public void onNext(List<ActivityViewModel> list) {
                view.setupList(list);
            }
        }));
    }

    @Override
    public void onRetryClicked() {
        fetchActivities();
    }
}