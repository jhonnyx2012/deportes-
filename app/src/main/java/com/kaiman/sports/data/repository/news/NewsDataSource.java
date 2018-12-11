package com.kaiman.sports.data.repository.news;

import com.kaiman.sports.main.news.CommentViewModel;
import com.kaiman.sports.main.news.PublicationViewModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public interface NewsDataSource {
    Observable<List<PublicationViewModel>> getNews();

    Completable postPublication(String description, String image);

    Observable<String> postReaction(String publicationId);

    Completable deleteReaction(String reactionId);

    Observable<List<CommentViewModel>> getComments(String newsId);

    Completable deleteComment(String commentId);

    Observable<CommentViewModel> postComment(String newsId, String comment);

    Completable deletePublication(String id);
}