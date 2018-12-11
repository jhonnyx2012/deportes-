package com.kaiman.sports.main.news.presenter;

import android.support.annotation.NonNull;

import com.core.presentation.BasePresenter;
import com.kaiman.sports.data.remote.DisposableCompletableObserver;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.news.NewsDataSource;
import com.kaiman.sports.main.news.CommentViewModel;
import com.kaiman.sports.main.news.contract.Comments;

import java.util.List;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class CommentsPresenter extends BasePresenter<Comments.View> implements Comments.Presenter {
    private NewsDataSource newsRepository;
    private String newsId;

    public CommentsPresenter(@NonNull NewsDataSource newsRepository, String newsId) {
        this.newsRepository = newsRepository;
        this.newsId = newsId;
    }

    @Override
    public void initialize(Comments.View view) {
        super.initialize(view);
        fetchComments();
    }

    private void fetchComments() {
        view.showProgress();
        addSubscription(newsRepository.getComments(newsId).subscribeWith(new DisposableObserver<List<CommentViewModel>>(view) {
            @Override
            public void onNext(List<CommentViewModel> result) {
                if (result.isEmpty()) {
                    view.showNoCommentsFound();
                } else {
                    view.setupList(result);
                }
            }
        }));
    }

    @Override
    public void onRetryClicked() {
        fetchComments();
    }

    @Override
    public void onDeleteCommentClicked(int position, CommentViewModel item) {
        view.showConfirmDeleteCommentDialog(position, item);
    }

    @Override
    public void onSendClicked(String comment) {
        if (comment.trim().isEmpty()) {
            return;
        }
        view.showProgress();
        addSubscription(newsRepository.postComment(newsId, comment).subscribeWith(new DisposableObserver<CommentViewModel>(view) {
            @Override
            public void onNext(CommentViewModel commentViewModel) {
                view.clearInput();
                view.addComment(commentViewModel);
            }
        }));
    }

    @Override
    public void deleteComment(final int position, CommentViewModel item) {
        view.showProgress();
        clearSubscriptions();
        addSubscription(newsRepository.deleteComment(item.id).subscribeWith(new DisposableCompletableObserver(view) {
            @Override
            public void onComplete() {
                view.removeCommentByPosition(position);
            }

            @Override
            public void onError(Throwable e) {
                view.showDeleteErrorMessage();
            }
        }));
    }

    @Override
    public void refreshContent() {
        fetchComments();
    }
}
