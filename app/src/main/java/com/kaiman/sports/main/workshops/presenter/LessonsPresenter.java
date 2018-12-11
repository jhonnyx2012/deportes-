package com.kaiman.sports.main.workshops.presenter;

import android.support.annotation.NonNull;
import com.core.presentation.BasePresenter;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.user.local.UserLocalDataSource;
import com.kaiman.sports.data.repository.workshop.WorkshopDataSource;
import com.kaiman.sports.main.workshops.contract.Lessons;
import com.kaiman.sports.main.workshops.model.LessonViewModel;
import java.util.List;

/**
 * Created by jhonnybarrios on 3/21/18
 */

public class LessonsPresenter extends BasePresenter<Lessons.View> implements Lessons.Presenter {

    private final WorkshopDataSource workshopRepository;
    private final UserLocalDataSource userLocalRepository;
    private String workshopId;
    private String workshopTitle;
    private boolean canCreateLessons;

    public LessonsPresenter(@NonNull WorkshopDataSource workshopRepository, @NonNull UserLocalDataSource userLocalRepository) {
        this.workshopRepository = workshopRepository;
        this.userLocalRepository = userLocalRepository;
    }

    @Override
    public void initialize(Lessons.View view) {
        super.initialize(view);
        canCreateLessons = userLocalRepository.getLoggedUser().canCreateLessons();
        view.updateTitle(workshopTitle);
        if (!canCreateLessons) {
            view.setupBasicUserView();
        }
    }

    private void fetchLessons() {
        view.showProgress();
        workshopRepository.getLessons(workshopId).subscribeWith(new DisposableObserver<List<LessonViewModel>>(view) {
            @Override
            public void onNext(List<LessonViewModel> lessonViewModels) {
                if (lessonViewModels.isEmpty()) {
                    view.showNoLessonsPlaceholder();
                } else {
                    view.setupList(lessonViewModels, canCreateLessons);
                }
            }
        });
    }

    @Override
    public void onRetryClicked() {
        fetchLessons();
    }

    @Override
    public void setWorkshopData(String workshopId, String workshopTitle) {
        this.workshopId = workshopId;
        this.workshopTitle = workshopTitle;

    }

    @Override
    public void refreshContent() {
        fetchLessons();
    }
}