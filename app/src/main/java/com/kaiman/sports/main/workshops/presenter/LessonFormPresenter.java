package com.kaiman.sports.main.workshops.presenter;

import android.support.annotation.NonNull;
import com.core.presentation.BasePresenter;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.user.local.UserLocalDataSource;
import com.kaiman.sports.data.repository.workshop.WorkshopDataSource;
import com.kaiman.sports.main.workshops.contract.LessonForm;
import com.kaiman.sports.main.workshops.model.LessonViewModel;
import com.kaiman.sports.utils.Utils;

import io.reactivex.Observable;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class LessonFormPresenter extends BasePresenter<LessonForm.View> implements LessonForm.Presenter {
    private final UserLocalDataSource userLocalRepository;
    private WorkshopDataSource workshopRepository;
    private String lessonId;
    private String workshopId;

    public LessonFormPresenter(@NonNull WorkshopDataSource workshopRepository,  @NonNull UserLocalDataSource userLocalRepository) {
        this.workshopRepository = workshopRepository;
        this.userLocalRepository = userLocalRepository;
    }

    @Override
    public void initialize(LessonForm.View view) {
        super.initialize(view);
        view.setUserCanEditLesson(userLocalRepository.getLoggedUser().canEditLesson());
        fetchLesson(lessonId);
    }

    private void fetchLesson(String lessonId) {
        if (lessonId == null) {
            return;
        }
        view.showProgress();
        addSubscription(workshopRepository.getLesson(lessonId)
                .subscribeWith(new DisposableObserver<LessonViewModel>(view) {
                    @Override
                    public void onNext(LessonViewModel lesson) {
                        view.hideProgress();
                        view.setupLesson(lesson);
                    }
                }));
    }

    private void doSave(String lessonTitle, String startDate, String startHour, String endHour, String objetive, String content) {
        view.showProgress();
        Observable<LessonViewModel> observable;
        if (view.isOnEditMode()) {
            observable = workshopRepository.updateLesson(lessonId, lessonTitle, startDate, startHour, endHour, objetive, content);
        } else {
            observable = workshopRepository.createLesson(workshopId, lessonTitle, startDate, startHour, endHour, objetive, content);
        }

        addSubscription(observable.subscribeWith(new DisposableObserver<LessonViewModel>(view) {
            @Override
            public void onNext(LessonViewModel lesson) {
                view.hideProgress();
                view.goBack();
                view.showSuccessCreationMessage();
            }
        }));
    }

    @Override
    public void onRetryClicked() {
        fetchLesson(lessonId);
    }

    @Override
    public void setLessonId(String id) {
        this.lessonId = id;
    }

    @Override
    public void attemptSave(String lessonTitle, String startDate, String startHour, String endHour, String objetive, String content) {
        view.clearErrorFromFields();

        if (!Utils.nonNullOrEmpty(lessonTitle)) {
            view.showTitleEmptyError();

        } else if (!Utils.nonNullOrEmpty(startDate)) {
            view.showStartDateEmptyError();

        } else if (!Utils.nonNullOrEmpty(startHour)) {
            view.showStartHourEmptyError();

        } else if (!Utils.nonNullOrEmpty(endHour)) {
            view.showEndHourEmptyError();

        } else if (!Utils.nonNullOrEmpty(objetive)) {
            view.showObjetiveEmptyError();

        } else if (!Utils.nonNullOrEmpty(content)) {
            view.showContentEmptyError();
        } else {
            doSave(lessonTitle, startDate, startHour, endHour, objetive, content);
        }
    }

    @Override
    public void setWorkshopId(String id) {
        this.workshopId = id;
    }
}
