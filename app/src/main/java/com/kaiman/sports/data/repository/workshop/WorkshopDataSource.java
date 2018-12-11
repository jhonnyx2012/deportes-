package com.kaiman.sports.data.repository.workshop;

import com.kaiman.sports.main.workshops.model.DisciplineViewModel;
import com.kaiman.sports.main.workshops.model.LessonMemberViewModel;
import com.kaiman.sports.main.workshops.model.LessonViewModel;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;
import java.util.List;
import io.reactivex.Observable;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public interface WorkshopDataSource {
    Observable<List<WorkshopViewModel>> getMyWorkshops();
    Observable<List<WorkshopViewModel>> getWorkshops(String workshopTypeId, String disciplineId);
    Observable<List<DisciplineViewModel>> getDisciplines(String workshopTypeId);
    Observable<List<LessonViewModel>>  getLessons(String workshopId);
    Observable<WorkshopViewModel> getWorkshop(String workshopId);
    Observable<Boolean> checkIfImSubscribed(String workshopId);
    Observable<Boolean> subscribeToWorkshop(String workshopId);

    Observable<List<LessonMemberViewModel>> getLessonMembers(String lessonId);

    Observable<LessonMemberViewModel> changeMemberAttendance(String lessonMemberId, boolean attend);

    Observable<LessonViewModel> getLesson(String lessonId);

    Observable<LessonViewModel> createLesson(String workshopId, String lessonTitle, String startDate, String startHour, String endHour, String objetive, String content);
    Observable<LessonViewModel> updateLesson(String lessonId, String lessonTitle, String startDate, String startHour, String endHour, String objetive, String content);
}