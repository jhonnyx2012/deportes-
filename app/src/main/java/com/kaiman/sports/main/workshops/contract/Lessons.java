package com.kaiman.sports.main.workshops.contract;

import com.core.presentation.contract.BaseViewPresenter;
import com.core.presentation.contract.BaseView;
import com.kaiman.sports.main.workshops.model.LessonViewModel;

import java.util.List;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public interface Lessons {
    interface View extends BaseView.RequestList<LessonViewModel> {
        void updateTitle(String workshopTitle);

        void showNoLessonsPlaceholder();

        void setupBasicUserView();

        void setupList(List<LessonViewModel> lessonViewModels, boolean userIsMonitor);
    }

    interface Presenter extends BaseViewPresenter<View> {
        void onRetryClicked();

        void setWorkshopData(String workshopId, String workshopTitle);

        void refreshContent();
    }
}
