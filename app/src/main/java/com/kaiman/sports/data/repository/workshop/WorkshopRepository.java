package com.kaiman.sports.data.repository.workshop;

import android.support.annotation.NonNull;
import com.core.data.remote.SchedulerProvider;
import com.kaiman.sports.data.entity.DisciplineEntity;
import com.kaiman.sports.data.entity.LessonEntity;
import com.kaiman.sports.data.entity.LessonMemberEntity;
import com.kaiman.sports.data.entity.MemberEntity;
import com.kaiman.sports.data.entity.User;
import com.kaiman.sports.data.entity.WorkshopEntity;
import com.kaiman.sports.data.mapper.DisciplineEntityMapper;
import com.kaiman.sports.data.mapper.LessonEntityMapper;
import com.kaiman.sports.data.mapper.LessonMemberEntityMapper;
import com.kaiman.sports.data.mapper.WorkshopEntityMapper;
import com.kaiman.sports.data.remote.ApiService;
import com.kaiman.sports.data.remote.response.ApiResponse;
import com.kaiman.sports.data.repository.user.local.UserLocalDataSource;
import com.kaiman.sports.main.workshops.model.DisciplineViewModel;
import com.kaiman.sports.main.workshops.model.LessonMemberViewModel;
import com.kaiman.sports.main.workshops.model.LessonViewModel;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class WorkshopRepository implements WorkshopDataSource {
    private final SchedulerProvider schedulerProvider;
    private final DisciplineEntityMapper disciplineMapper;
    private final UserLocalDataSource userLocalDataSource;
    private ApiService apiService;
    private WorkshopEntityMapper mapper;
    private LessonEntityMapper lessonsMapper;
    private LessonMemberEntityMapper lessonMemberMapper;

    public WorkshopRepository(@NonNull ApiService apiService,
                              @NonNull WorkshopEntityMapper mapper,
                              @NonNull DisciplineEntityMapper disciplineMapper,
                              @NonNull LessonEntityMapper lessonsMapper,
                              @NonNull LessonMemberEntityMapper lessonMemberMapper,
                              @NonNull UserLocalDataSource userLocalDataSource,
                              @NonNull SchedulerProvider schedulerProvider) {
        this.apiService = apiService;
        this.mapper = mapper;
        this.schedulerProvider = schedulerProvider;
        this.disciplineMapper = disciplineMapper;
        this.lessonsMapper = lessonsMapper;
        this.lessonMemberMapper = lessonMemberMapper;
        this.userLocalDataSource = userLocalDataSource;
    }

    @Override
    public Observable<List<WorkshopViewModel>> getWorkshops(String workshopTypeId, String disciplineId) {
        return apiService.getWorkshops(workshopTypeId, disciplineId)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<List<WorkshopEntity>>, List<WorkshopViewModel>>() {
                    @Override
                    public List<WorkshopViewModel> apply(ApiResponse<List<WorkshopEntity>> workshopEntities) throws Exception {
                        return mapper.map(workshopEntities.data);
                    }
                });
    }

    @Override
    public Observable<List<DisciplineViewModel>> getDisciplines(String workshopTypeId) {
        return apiService.getDisciplines(workshopTypeId)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<List<DisciplineEntity>>, List<DisciplineViewModel>>() {
                    @Override
                    public List<DisciplineViewModel> apply(ApiResponse<List<DisciplineEntity>> response) throws Exception {
                        return disciplineMapper.map(response.data);
                    }
                });
    }

    @Override
    public Observable<List<WorkshopViewModel>> getMyWorkshops() {
        Observable<List<WorkshopViewModel>> observable = getMyWorkshopsInternal();
        return observable.flatMap(new Function<List<WorkshopViewModel>, Observable<WorkshopViewModel>>() {
            @Override
            public Observable<WorkshopViewModel> apply(List<WorkshopViewModel> list) {
                return Observable.fromIterable(list);
            }
        }).toSortedList(new Comparator<WorkshopViewModel>() {
            @Override
            public int compare(WorkshopViewModel o1, WorkshopViewModel o2) {
                return o1.title.compareTo(o2.title);
            }
        }).toObservable();
    }

    private Observable<List<WorkshopViewModel>> getMyWorkshopsInternal() {
        User user = userLocalDataSource.getLoggedUser();
        if (user.permission_id == null) {
            return Observable.error(new Exception("user_permission_not_defined"));
        }
        if (user.isBasicUser()) {
            return getBasicUserWorkshops(user.id);
        } else if (user.isMonitor()) {
            return apiService.getMonitorWorkshops(user.id)
                    .observeOn(schedulerProvider.observer())
                    .subscribeOn(schedulerProvider.subscriber())
                    .map(new Function<ApiResponse<List<WorkshopEntity>>, List<WorkshopViewModel>>() {
                        @Override
                        public List<WorkshopViewModel> apply(ApiResponse<List<WorkshopEntity>> listApiResponse) throws Exception {
                            return mapper.map(listApiResponse.data);
                        }
                    });
        } else if (user.isAdministrator()) {
            return apiService.getWorkshops(null, null)
                    .observeOn(schedulerProvider.observer())
                    .subscribeOn(schedulerProvider.subscriber())
                    .map(new Function<ApiResponse<List<WorkshopEntity>>, List<WorkshopViewModel>>() {
                        @Override
                        public List<WorkshopViewModel> apply(ApiResponse<List<WorkshopEntity>> listApiResponse) throws Exception {
                            return mapper.map(listApiResponse.data);
                        }
                    });
        } else {
            return Observable.error(new Exception("user_permission_not_recognized"));
        }
    }

    private Observable<List<WorkshopViewModel>> getBasicUserWorkshops(String userId) {
        return apiService.getMembersByUserId(userId, null)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .flatMap(new Function<ApiResponse<List<MemberEntity>>, Observable<ApiResponse<List<WorkshopEntity>>>>() {
                    @Override
                    public Observable<ApiResponse<List<WorkshopEntity>>> apply(ApiResponse<List<MemberEntity>> listApiResponse) throws Exception {
                        if (listApiResponse.data.isEmpty()){
                            ApiResponse<List<WorkshopEntity>> response = new ApiResponse<>();
                            response.data = Collections.emptyList();
                            return Observable.just(response);
                        }
                        return apiService.getWorkshopsByIds(getWorkshopsIdsFromMembers(listApiResponse.data))
                                .observeOn(schedulerProvider.observer())
                                .subscribeOn(schedulerProvider.subscriber());
                    }
                }).map(new Function<ApiResponse<List<WorkshopEntity>>, List<WorkshopViewModel>>() {
                    @Override
                    public List<WorkshopViewModel> apply(ApiResponse<List<WorkshopEntity>> listApiResponse) throws Exception {
                        return mapper.map(listApiResponse.data);
                    }
                });
    }

    private List<String> getWorkshopsIdsFromMembers(List<MemberEntity> members) {
        List<String> ids = new ArrayList<>();
        for (MemberEntity member : members) {
            ids.add(member.workshop_id);
        }
        return ids;
    }

    @Override
    public Observable<List<LessonViewModel>> getLessons(String workshopId) {
        return apiService.getLessons(workshopId)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<List<LessonEntity>>, List<LessonViewModel>>() {
                    @Override
                    public List<LessonViewModel> apply(ApiResponse<List<LessonEntity>> listApiResponse) throws Exception {
                        return lessonsMapper.map(listApiResponse.data);
                    }
                });
    }

    @Override
    public Observable<WorkshopViewModel> getWorkshop(String workshopId) {
        return apiService.getWorkshop(workshopId)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<WorkshopEntity>, WorkshopViewModel>() {
                    @Override
                    public WorkshopViewModel apply(ApiResponse<WorkshopEntity> listApiResponse) throws Exception {
                        return mapper.map(listApiResponse.data);
                    }
                });
    }

    @Override
    public Observable<Boolean> checkIfImSubscribed(String workshopId) {
        String userId = userLocalDataSource.getLoggedUser().id;
        return apiService.getMembersByUserId(userId, workshopId)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<List<MemberEntity>>, Boolean>() {
                    @Override
                    public Boolean apply(ApiResponse<List<MemberEntity>> listApiResponse) throws Exception {
                        return !listApiResponse.data.isEmpty();
                    }
                });
    }

    @Override
    public Observable<Boolean> subscribeToWorkshop(String workshopId) {
        String userId = userLocalDataSource.getLoggedUser().id;
        return apiService.subscribeToWorkshop(userId, workshopId)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<MemberEntity>, Boolean>() {
                    @Override
                    public Boolean apply(ApiResponse<MemberEntity> response) throws Exception {
                        return response.data != null;
                    }
                });
    }

    @Override
    public Observable<List<LessonMemberViewModel>> getLessonMembers(String lessonId) {
        return apiService.getLessonMembers(lessonId)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<List<LessonMemberEntity>>, List<LessonMemberViewModel>>() {
                    @Override
                    public List<LessonMemberViewModel> apply(ApiResponse<List<LessonMemberEntity>> listApiResponse) throws Exception {
                        return lessonMemberMapper.map(listApiResponse.data);
                    }
                });
    }

    @Override
    public Observable<LessonMemberViewModel> changeMemberAttendance(String lessonMemberId, boolean attend) {
        return apiService.updateLessonMemberAttendance(lessonMemberId, attend)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<LessonMemberEntity>, LessonMemberViewModel>() {
                    @Override
                    public LessonMemberViewModel apply(ApiResponse<LessonMemberEntity> lessonMemberEntityApiResponse) throws Exception {
                        return lessonMemberMapper.map(lessonMemberEntityApiResponse.data);
                    }
                });
    }

    @Override
    public Observable<LessonViewModel> getLesson(String lessonId) {
        return apiService.getLesson(lessonId)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<LessonEntity>, LessonViewModel>() {
                    @Override
                    public LessonViewModel apply(ApiResponse<LessonEntity> lessonEntityApiResponse) throws Exception {
                        return lessonsMapper.map(lessonEntityApiResponse.data);
                    }
                });
    }

    @Override
    public Observable<LessonViewModel> createLesson(String workshopId, String lessonTitle, String startDate, String startHour, String endHour, String objetive, String content) {
        return apiService.createLesson(lessonTitle, workshopId, startDate, startHour, endHour, objetive, content)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<LessonEntity>, LessonViewModel>() {
                    @Override
                    public LessonViewModel apply(ApiResponse<LessonEntity> lessonEntityApiResponse) throws Exception {
                        return lessonsMapper.map(lessonEntityApiResponse.data);
                    }
                });
    }

    @Override
    public Observable<LessonViewModel> updateLesson(String lessonId, String lessonTitle, String startDate, String startHour, String endHour, String objetive, String content) {
        return apiService.updateLesson(lessonId, lessonTitle, startDate, startHour, endHour, objetive, content)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<LessonEntity>, LessonViewModel>() {
                    @Override
                    public LessonViewModel apply(ApiResponse<LessonEntity> lessonEntityApiResponse) throws Exception {
                        return lessonsMapper.map(lessonEntityApiResponse.data);
                    }
                });
    }
}