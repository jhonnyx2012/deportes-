package com.kaiman.sports.main.workshops.presenter;

import android.support.annotation.NonNull;

import com.core.presentation.BasePresenter;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.workshop.WorkshopDataSource;
import com.kaiman.sports.main.workshops.contract.Attendance;
import com.kaiman.sports.main.workshops.contract.Lessons;
import com.kaiman.sports.main.workshops.model.LessonMemberViewModel;
import com.kaiman.sports.main.workshops.model.LessonViewModel;

import java.util.List;

/**
 * Created by jhonnybarrios on 3/21/18
 */

public class AttendancePresenter extends BasePresenter<Attendance.View> implements Attendance.Presenter {

    private final WorkshopDataSource workshopRepository;
    private String lessonId;

    public AttendancePresenter(@NonNull WorkshopDataSource workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    @Override
    public void initialize(Attendance.View view) {
        super.initialize(view);
        fetchLessonMembers();
    }

    private void fetchLessonMembers() {
        view.showProgress();
        workshopRepository.getLessonMembers(lessonId).subscribeWith(new DisposableObserver<List<LessonMemberViewModel>>(view) {
            @Override
            public void onNext(List<LessonMemberViewModel> response) {
                if (response.isEmpty()) {
                    view.showNoRegisteredMembersPlaceholder();
                } else {
                    view.setupList(response);
                }
            }
        });
    }

    @Override
    public void onRetryClicked() {
        fetchLessonMembers();
    }

    @Override
    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    @Override
    public void toggleMemberAttendance(final int position, LessonMemberViewModel item) {
        view.showProgress();
        workshopRepository.changeMemberAttendance(item.id, !item.attend).subscribeWith(new DisposableObserver<LessonMemberViewModel>(view) {
            @Override
            public void onNext(LessonMemberViewModel response) {
                view.hideProgress();
                view.updateMember(position, response);
            }
        });
    }
}