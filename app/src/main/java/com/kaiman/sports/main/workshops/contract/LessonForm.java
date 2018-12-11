package com.kaiman.sports.main.workshops.contract;

import com.core.presentation.contract.BaseView;
import com.core.presentation.contract.BaseViewPresenter;
import com.kaiman.sports.main.workshops.model.LessonViewModel;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public interface LessonForm {
    interface View extends BaseView.Request {
       void setupLesson(LessonViewModel lesson);
        void clearErrorFromFields();
        String getLessonTitle();
        String getStartDate();
        String getStartHour();
        String getEndHour();
        String getObjetive();
        String getContent();

        void showSuccessCreationMessage();

        void showTitleEmptyError();

        void showStartDateEmptyError();

        void showStartHourEmptyError();

        void showEndHourEmptyError();

        void showObjetiveEmptyError();

        void showContentEmptyError();

        void goBack();

        void setUserCanEditLesson(boolean isMonitor);

        boolean isOnEditMode();
    }

    interface Presenter extends BaseViewPresenter<View> {
        void onRetryClicked();

        void setLessonId(String id);

        void attemptSave(String lessonTitle, String startDate, String startHour, String endHour, String objetive, String content);

        void setWorkshopId(String id);
    }
}