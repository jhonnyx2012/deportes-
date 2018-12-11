package com.kaiman.sports.data.remote;

import com.kaiman.sports.data.entity.CommentEntity;
import com.kaiman.sports.data.entity.DeviceRegistration;
import com.kaiman.sports.data.entity.DisciplineEntity;
import com.kaiman.sports.data.entity.LessonEntity;
import com.kaiman.sports.data.entity.LessonMemberEntity;
import com.kaiman.sports.data.entity.MemberEntity;
import com.kaiman.sports.data.entity.MobileVersion;
import com.kaiman.sports.data.entity.NewsEntity;
import com.kaiman.sports.data.entity.NewsReaction;
import com.kaiman.sports.data.entity.User;
import com.kaiman.sports.data.entity.WorkshopEntity;
import com.kaiman.sports.data.remote.response.ApiResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public interface ApiService {
    String API_VERSION = "v1/";
    String URL_BASE = "http://www.deportesmas.cl/"+API_VERSION;
    String ENDPOINT_LOGIN = "auth/sign_in";
    String ENDPOINT_REGISTER = "auth";
    String ENDPOINT_RESET_PASSWORD = "reset_passwords";
    String ENDPOINT_WORKSHOPS = "workshops";
    String ENDPOINT_DISCIPLINES = "disciplines";
    String ENDPOINT_MEMBERS = "members";
    String ENDPOINT_LESSONS = "lessons";
    String ENDPOINT_LESSON_MEMBERS = "lesson_members";
    String ENDPOINT_REGISTRATION_DEVICE = "registration_devices";
    String ENDPOINT_NEWS = "news";
    String ENDPOINT_USERS = "users";
    String ENDPOINT_REACTIONS = "new_reactions";
    String ENDPOINT_COMMENTS = "commentaries";
    String ENDPOINT_MOBILE_VERSIONS = "mobile_versions";

    @FormUrlEncoded
    @POST(ENDPOINT_LOGIN)
    Observable<ApiResponse<User>> login(@Field("email") String email,
                                        @Field("password") String password);

    @FormUrlEncoded
    @POST(ENDPOINT_REGISTER)
    Observable<ApiResponse<User>> register(@Field("email") String email,
                                           @Field("password") String password,
                                           @Field("password_confirmation") String password_confirmation);

    @GET(ENDPOINT_WORKSHOPS)
    Observable<ApiResponse<List<WorkshopEntity>>> getWorkshops(
            @Query("q[workshop_type_id_eq]") String workshopTypeId,
            @Query("q[discipline_id_eq]") String disciplineId);

    @GET(ENDPOINT_WORKSHOPS + "/{id}")
    Observable<ApiResponse<WorkshopEntity>> getWorkshop(@Path("id") String workshopId);

    @GET(ENDPOINT_WORKSHOPS)
    Observable<ApiResponse<List<WorkshopEntity>>> getWorkshopsByIds(@Query("q[id_in][]") List<String> ids);

    @GET(ENDPOINT_WORKSHOPS)
    Observable<ApiResponse<List<WorkshopEntity>>> getMonitorWorkshops(@Query("q[monitor_id_eq]") String monitorId);

    @GET(ENDPOINT_MEMBERS)
    Observable<ApiResponse<List<MemberEntity>>> getMembersByUserId(
            @Query("q[user_id_eq]") String userId,
            @Query("q[workshop_id_eq]") String workshopId);


    @FormUrlEncoded
    @POST(ENDPOINT_MEMBERS)
    Observable<ApiResponse<MemberEntity>> subscribeToWorkshop(
            @Field("user_id]") String userId,
            @Field("workshop_id") String workshopId);

    @GET(ENDPOINT_DISCIPLINES)
    Observable<ApiResponse<List<DisciplineEntity>>> getDisciplines(@Query("in_workshop_type_id") String workshopTypeId);

    @GET(ENDPOINT_LESSONS)
    Observable<ApiResponse<List<LessonEntity>>> getLessons(@Query("q[workshop_id_eq]") String workshopId);

    @FormUrlEncoded
    @POST(ENDPOINT_LESSONS)
    Observable<ApiResponse<LessonEntity>> createLesson(@Field("title") String title,
                                                       @Field("workshop_id") String workshop_id,
                                                       @Field("start_date") String start_date,
                                                       @Field("start_time") String start_time,
                                                       @Field("end_time") String end_time,
                                                       @Field("objetive") String objetive,
                                                       @Field("content") String content);

    @FormUrlEncoded
    @PATCH(ENDPOINT_LESSONS+ "/{id}")
    Observable<ApiResponse<LessonEntity>> updateLesson(@Path("id") String lessonId,
                                                       @Field("title") String title,
                                                       @Field("start_date") String start_date,
                                                       @Field("start_time") String start_time,
                                                       @Field("end_time") String end_time,
                                                       @Field("objetive") String objetive,
                                                       @Field("content") String content);
    @GET(ENDPOINT_LESSONS + "/{id}")
    Observable<ApiResponse<LessonEntity>> getLesson(@Path("id") String lessonId);

    @GET(ENDPOINT_LESSON_MEMBERS)
    Observable<ApiResponse<List<LessonMemberEntity>>> getLessonMembers(@Query("q[lesson_id_eq]") String lessonId);

    @FormUrlEncoded
    @PATCH(ENDPOINT_LESSON_MEMBERS + "/{id}")
    Observable<ApiResponse<LessonMemberEntity>> updateLessonMemberAttendance(@Path("id") String lessonMemberId,
                                                                                   @Field("attend") boolean attend);

    @FormUrlEncoded
    @POST(ENDPOINT_REGISTRATION_DEVICE)
    Observable<ApiResponse<DeviceRegistration>> registerDevice(@Field("user_id") String userId, @Field("device_id") String deviceId);

    @DELETE(ENDPOINT_REGISTRATION_DEVICE  + "/{id}")
    Completable deleteDevice(@Path("id") String registrationId);

    @GET(ENDPOINT_NEWS)
    Observable<ApiResponse<List<NewsEntity>>> getNews();

    @FormUrlEncoded
    @POST(ENDPOINT_NEWS)
    Observable<ApiResponse<NewsEntity>> postPublication(@Field("user_id") String userId, @Field("description") String description, @Field("image") String image);

    @GET(ENDPOINT_REACTIONS)
    Observable<ApiResponse<List<NewsReaction>>> getReactions(@Query("q[new_id_in][]") List<String> publicationsIds,@Query("q[user_id_in]") String userId);

    @FormUrlEncoded
    @POST(ENDPOINT_REACTIONS)
    Observable<ApiResponse<NewsReaction>> postReaction(@Field("user_id") String user_id, @Field("new_id") String newsId,  @Field("reaction") String reaction);

    @DELETE(ENDPOINT_REACTIONS  + "/{id}")
    Completable deleteReaction(@Path("id") String reactionId);

    @GET(ENDPOINT_COMMENTS)
    Observable<ApiResponse<List<CommentEntity>>> getComments(@Query("q[new_id_eq]") int newId);

    @FormUrlEncoded
    @POST(ENDPOINT_COMMENTS)
    Observable<ApiResponse<CommentEntity>> postComment(@Field("user_id") String userId, @Field("new_id") String newsId, @Field("commentary") String commentary);

    @DELETE(ENDPOINT_COMMENTS  + "/{id}")
    Completable deleteComment(@Path("id") String commentId);


    @DELETE(ENDPOINT_NEWS  + "/{id}")
    Completable deleteNews(@Path("id") String id);

    @FormUrlEncoded
    @PUT(ENDPOINT_USERS  + "/{id}")
    Observable<ApiResponse<User>> editUser(@Path("id") String userId,
                         @Field("rut") String rut,
                         @Field("first_name") String firstName,
                         @Field("last_name") String lastName,
                         @Field("address") String address,
                         @Field("password") String password,
                         @Field("password_confirmation") String passwordConfirmation);

    @FormUrlEncoded
    @PUT(ENDPOINT_USERS  + "/{id}")
    Observable<ApiResponse<User>> updateUserImage(@Path("id") String userId,
                                           @Field("image") String imageUrl);

    @GET(ENDPOINT_MOBILE_VERSIONS + "?q[id_eq]=2")
    Observable<ApiResponse<List<MobileVersion>>> fetchAppVersion();

    @FormUrlEncoded
    @POST(ENDPOINT_RESET_PASSWORD)
    Completable resetPassword(@Field("email") String userId);
}