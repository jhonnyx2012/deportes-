package com.kaiman.sports.data.repository.news;

import android.support.annotation.NonNull;

import com.core.data.remote.SchedulerProvider;
import com.kaiman.sports.data.entity.CommentEntity;
import com.kaiman.sports.data.entity.NewsEntity;
import com.kaiman.sports.data.entity.NewsReaction;
import com.kaiman.sports.data.entity.User;
import com.kaiman.sports.data.mapper.CommentEntityMapper;
import com.kaiman.sports.data.mapper.NewsEntityMapper;
import com.kaiman.sports.data.remote.ApiService;
import com.kaiman.sports.data.remote.response.ApiResponse;
import com.kaiman.sports.data.repository.user.UserDataSource;
import com.kaiman.sports.main.news.CommentViewModel;
import com.kaiman.sports.main.news.PublicationViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class NewsRepository implements NewsDataSource {

    private final ApiService apiService;
    private final SchedulerProvider schedulerProvider;
    private final NewsEntityMapper mapper;
    private final UserDataSource userRepository;
    private final CommentEntityMapper commentMapper;

    public NewsRepository(@NonNull ApiService apiService,
                          @NonNull NewsEntityMapper mapper,
                          @NonNull CommentEntityMapper commentMapper,
                          @NonNull UserDataSource userRepository,
                          @NonNull SchedulerProvider schedulerProvider) {
        this.apiService = apiService;
        this.mapper = mapper;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.schedulerProvider = schedulerProvider;
    }


    private ArrayList<NewsEntity> publications;
    @Override
    public Observable<List<PublicationViewModel>> getNews() {
        publications = new ArrayList<>();
        final User loggedUser = userRepository.getLoggedUser();
        return apiService.getNews()
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .flatMap(new Function<ApiResponse<List<NewsEntity>>, Observable<ApiResponse<List<NewsReaction>>>>() {
                    @Override
                    public Observable<ApiResponse<List<NewsReaction>>> apply(ApiResponse<List<NewsEntity>> listApiResponse) throws Exception {
                        publications = new ArrayList<>(listApiResponse.data);
                        List<String> ids = getPublicationsIds(publications);
                        return apiService.getReactions(ids, loggedUser.id)
                                .observeOn(schedulerProvider.observer())
                                .subscribeOn(schedulerProvider.subscriber());
                    }
                }).map(new Function<ApiResponse<List<NewsReaction>>, List<PublicationViewModel>>() {
                    @Override
                    public List<PublicationViewModel> apply(ApiResponse<List<NewsReaction>> response) throws Exception {
                        for (int i = 0; i < publications.size(); i++) {
                            NewsReaction fakeReaction = new NewsReaction(publications.get(i).id, loggedUser.id);
                            int index = response.data.indexOf(fakeReaction);
                            if (index != -1){
                                publications.get(i).reaction_id = response.data.get(index).id;
                            }
                        }
                        mapper.setActualUser(loggedUser);
                        return mapper.map(publications);
                    }
                }).flatMap(new Function<List<PublicationViewModel>, Observable<PublicationViewModel>>() {
                    @Override
                    public Observable<PublicationViewModel> apply(List<PublicationViewModel> publicationViewModels) throws Exception {
                        return Observable.fromIterable(publicationViewModels);
                    }
                }).toSortedList(new Comparator<PublicationViewModel>() {
                    @Override
                    public int compare(PublicationViewModel o1, PublicationViewModel o2) {
                        return o1.date.getTime() > o2.date.getTime() ? -1 : 1;
                    }
                }).toObservable();
    }

    private List<String> getPublicationsIds(List<NewsEntity> publications) {
        List<String> result = new ArrayList<>();
        for (NewsEntity publication : publications) {
            result.add(publication.id);
        }
        return result;
    }

    @Override
    public Completable postPublication(String description, String image) {
        String userId = userRepository.getLoggedUser().id;
            return apiService.postPublication(userId, description, image)
                    .observeOn(schedulerProvider.observer())
                    .subscribeOn(schedulerProvider.subscriber())
                    .flatMapCompletable(new Function<ApiResponse<NewsEntity>, CompletableSource>() {
                        @Override
                        public CompletableSource apply(ApiResponse<NewsEntity> newsEntityApiResponse) throws Exception {
                            return Completable.complete();
                        }
                    });
    }

    @Override
    public Observable<String> postReaction(String publicationId) {
        String userId = userRepository.getLoggedUser().id;
        return apiService.postReaction(userId, publicationId, "like")
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<NewsReaction>, String>() {
                    @Override
                    public String apply(ApiResponse<NewsReaction> response) throws Exception {
                        return response.data.id;
                    }
                });
    }

    @Override
    public Completable deleteReaction(String reactionId) {
        return apiService.deleteReaction(reactionId)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber());
    }

    @Override
    public Observable<List<CommentViewModel>> getComments(String newsId) {
        String userId = userRepository.getLoggedUser().id;
        commentMapper.setActualUserId(userId);
        return apiService.getComments(Integer.valueOf(newsId))
                .map(new Function<ApiResponse<List<CommentEntity>>, List<CommentViewModel>>() {
                    @Override
                    public List<CommentViewModel> apply(ApiResponse<List<CommentEntity>> listApiResponse) {
                        return commentMapper.map(listApiResponse.data);
                    }
                })
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber());
    }

    @Override
    public Completable deleteComment(String commentId) {
        return apiService.deleteComment(commentId)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber());
    }

    @Override
    public Observable<CommentViewModel> postComment(String newsId, String comment) {
        String userId = userRepository.getLoggedUser().id;
        commentMapper.setActualUserId(userId);
        return apiService.postComment(userId, newsId, comment)
                .map(new Function<ApiResponse<CommentEntity>, CommentViewModel>() {
                    @Override
                    public CommentViewModel apply(ApiResponse<CommentEntity> apiResponse) {
                        return commentMapper.map(apiResponse.data);
                    }
                })
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber());
    }

    @Override
    public Completable deletePublication(String id) {
        return apiService.deleteNews(id)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber());
    }
}