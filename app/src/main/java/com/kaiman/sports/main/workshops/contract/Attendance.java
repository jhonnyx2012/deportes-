package com.kaiman.sports.main.workshops.contract;

import com.core.presentation.contract.BaseView;
import com.core.presentation.contract.BaseViewPresenter;
import com.kaiman.sports.main.workshops.model.LessonMemberViewModel;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public interface Attendance {
    interface View extends BaseView.RequestList<LessonMemberViewModel> {
        void updateMember(int position, LessonMemberViewModel item);

        void showNoRegisteredMembersPlaceholder();
    }

    interface Presenter extends BaseViewPresenter<View> {
        void onRetryClicked();

        void setLessonId(String lessonId);

        void toggleMemberAttendance(int position, LessonMemberViewModel item);
    }
}