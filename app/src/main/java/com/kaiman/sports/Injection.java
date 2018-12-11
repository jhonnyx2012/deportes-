package com.kaiman.sports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import com.core.data.remote.ApiServiceFactory;
import com.core.data.remote.SchedulerProvider;
import com.core.util.DialogHelper;
import com.core.util.PlaceholderRequestViewHelper;
import com.kaiman.sports.data.local.api.ApiAuthLocalDataSource;
import com.kaiman.sports.data.local.api.ApiAuthPreferences;
import com.kaiman.sports.data.mapper.CommentEntityMapper;
import com.kaiman.sports.data.mapper.DisciplineEntityMapper;
import com.kaiman.sports.data.mapper.LessonEntityMapper;
import com.kaiman.sports.data.mapper.LessonMemberEntityMapper;
import com.kaiman.sports.data.mapper.NewsEntityMapper;
import com.kaiman.sports.data.mapper.WorkshopEntityMapper;
import com.kaiman.sports.data.remote.ApiService;
import com.kaiman.sports.data.remote.AuthenticationInterceptor;
import com.kaiman.sports.data.remote.RuntimeSchedulerProvider;
import com.kaiman.sports.data.repository.activity.ActivityDataSource;
import com.kaiman.sports.data.repository.activity.DummyActivityRepository;
import com.kaiman.sports.data.repository.app.AppDataSource;
import com.kaiman.sports.data.repository.app.AppRepository;
import com.kaiman.sports.data.repository.news.NewsDataSource;
import com.kaiman.sports.data.repository.news.NewsRepository;
import com.kaiman.sports.data.repository.user.UserDataSource;
import com.kaiman.sports.data.repository.user.UserRepository;
import com.kaiman.sports.data.repository.user.local.LocalUserRepository;
import com.kaiman.sports.data.repository.workshop.WorkshopDataSource;
import com.kaiman.sports.data.repository.workshop.WorkshopRepository;
import com.kaiman.sports.intro.IntroRequestViewHelper;
import com.kaiman.sports.intro.login.Login;
import com.kaiman.sports.intro.login.LoginPresenter;
import com.kaiman.sports.intro.recover.ForgotPassword;
import com.kaiman.sports.intro.recover.ForgotPasswordPresenter;
import com.kaiman.sports.intro.register.Register;
import com.kaiman.sports.intro.register.RegisterPresenter;
import com.kaiman.sports.main.activities.Activities;
import com.kaiman.sports.main.activities.presenter.ActivitiesPresenter;
import com.kaiman.sports.main.news.contract.Comments;
import com.kaiman.sports.main.news.contract.CreateNews;
import com.kaiman.sports.main.news.contract.News;
import com.kaiman.sports.main.news.presenter.CommentsPresenter;
import com.kaiman.sports.main.news.presenter.CreateNewsPresenter;
import com.kaiman.sports.main.news.presenter.NewsPresenter;
import com.kaiman.sports.main.profile.contract.EditUser;
import com.kaiman.sports.main.profile.presenter.EditUserPresenter;
import com.kaiman.sports.main.workshops.contract.Attendance;
import com.kaiman.sports.main.workshops.contract.LessonForm;
import com.kaiman.sports.main.workshops.contract.Lessons;
import com.kaiman.sports.main.workshops.contract.MyWorkshops;
import com.kaiman.sports.main.workshops.contract.WorkshopDetail;
import com.kaiman.sports.main.workshops.contract.WorkshopList;
import com.kaiman.sports.main.workshops.presenter.AttendancePresenter;
import com.kaiman.sports.main.workshops.presenter.LessonFormPresenter;
import com.kaiman.sports.main.workshops.presenter.LessonsPresenter;
import com.kaiman.sports.main.workshops.presenter.MyWorkshopsPresenter;
import com.kaiman.sports.main.workshops.presenter.WorkshopDetailPresenter;
import com.kaiman.sports.main.workshops.presenter.WorkshopListPresenter;

import id.zelory.compressor.Compressor;
import okhttp3.OkHttpClient;

/**
 * Created by jhonnybarrios on 3/13/18
 */

public class Injection {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    static void init(Context context) {
        mContext = context;
    }

    public static Login.Presenter provideLoginPresenter() {
        return new LoginPresenter(provideUserRepository());
    }

    public static UserDataSource provideUserRepository() {
        return new UserRepository(provideUserLocalRepository(), proviceApiService(), provideSchedulerProvider());
    }

    public static SchedulerProvider provideSchedulerProvider() {
        return new RuntimeSchedulerProvider();
    }

    private static ApiService proviceApiService() {
        return ApiServiceFactory.build(provideOkHttpClient(),ApiService.class, ApiService.URL_BASE);
    }

    private static OkHttpClient provideOkHttpClient() {
        return ApiServiceFactory.buildOkHttpClient(new AuthenticationInterceptor(provideApiAuthPreferences()));
    }

    public static ApiAuthLocalDataSource provideApiAuthPreferences() {
        return new ApiAuthPreferences(mContext);
    }

    private static LocalUserRepository provideUserLocalRepository() {
        return new LocalUserRepository(mContext);
    }

    public static Register.Presenter provideRegisterPresenter() {
        return new RegisterPresenter(provideUserRepository());
    }

    public static DialogHelper provideDialogHelper() {
        return new DialogHelper(mContext);
    }

    public static PlaceholderRequestViewHelper providePlaceholderHelper(ViewGroup parent) {
        return new PlaceholderRequestViewHelper(parent);
    }

    public static IntroRequestViewHelper provideIntroRequestViewHelper(ViewGroup parent) {
        return new IntroRequestViewHelper(parent);
    }

    public static News.Presenter provideNewsPresenter() {
        return new NewsPresenter(provideNewsRepository(), provideUserLocalRepository());
    }

    private static NewsDataSource provideNewsRepository() {
        return new NewsRepository(proviceApiService(), provideNewsMapper(), provideCommentMapper(), provideUserRepository(), provideSchedulerProvider());
    }

    private static CommentEntityMapper provideCommentMapper() {
        return new CommentEntityMapper();
    }

    public static Compressor provideCompressor() {
        return new Compressor(mContext)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(75);
                //.setCompressFormat(Bitmap.CompressFormat.WEBP);
    }

    private static NewsEntityMapper provideNewsMapper() {
        return new NewsEntityMapper();
    }

    public static MyWorkshops.Presenter provideMyWorkshopsPresenter() {
        return new MyWorkshopsPresenter(provideWorkshopRepository());
    }

    private static WorkshopDataSource provideWorkshopRepository() {
        return new WorkshopRepository(proviceApiService(),
                provideWorkshopEntityMapper(),
                provideDisciplineEntityMapper(),
                provideLessonMapper(),
                provideLessonMemberMapper(),
                provideUserLocalRepository(),
                provideSchedulerProvider());
    }

    private static LessonMemberEntityMapper provideLessonMemberMapper() {
        return new LessonMemberEntityMapper();
    }

    private static LessonEntityMapper provideLessonMapper() {
        return new LessonEntityMapper();
    }

    private static DisciplineEntityMapper provideDisciplineEntityMapper() {
        return new DisciplineEntityMapper();
    }

    private static WorkshopEntityMapper provideWorkshopEntityMapper() {
        return new WorkshopEntityMapper();
    }

    public static WorkshopList.Presenter provideRecreativeWorkshopsPresenter() {
        return new WorkshopListPresenter(provideWorkshopRepository());
    }

    public static Activities.Presenter provideActivitiesPresenter() {
        return new ActivitiesPresenter(provideActivitiesRepository());
    }

    private static ActivityDataSource provideActivitiesRepository() {
        return new DummyActivityRepository();
    }

    public static Lessons.Presenter provideLessonsPresenter() {
        return new LessonsPresenter(provideWorkshopRepository(), provideUserLocalRepository());
    }

    public static WorkshopDetail.Presenter provideWorkshopDetailPresenter() {
        return new WorkshopDetailPresenter(provideWorkshopRepository(), provideUserLocalRepository());
    }

    public static Attendance.Presenter provideAttendancePresenter() {
        return new AttendancePresenter(provideWorkshopRepository());
    }

    public static LessonForm.Presenter provideLessonFormPresenter() {
        return new LessonFormPresenter(provideWorkshopRepository(), provideUserLocalRepository());
    }

    public static CreateNews.Presenter provideCreateNewsPresenter() {
        return new CreateNewsPresenter(provideNewsRepository());
    }

    public static EditUser.Presenter provideEditUserPresenter() {
        return new EditUserPresenter(provideUserRepository());
    }

    public static AppDataSource provideAppRepository() {
        return new AppRepository(proviceApiService(), provideSchedulerProvider());
    }

    public static Comments.Presenter provideCommentsPresenter(String newsId) {
        return new CommentsPresenter(provideNewsRepository(), newsId);
    }

    public static ForgotPassword.Presenter provideRecoverPasswordPresenter() {
        return new ForgotPasswordPresenter(provideUserRepository());
    }
}
