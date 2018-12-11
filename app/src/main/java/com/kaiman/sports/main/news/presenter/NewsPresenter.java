package com.kaiman.sports.main.news.presenter;

import android.support.annotation.NonNull;
import com.core.presentation.BasePresenter;
import com.kaiman.sports.data.remote.DisposableCompletableObserver;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.news.NewsDataSource;
import com.kaiman.sports.data.repository.user.local.LocalUserRepository;
import com.kaiman.sports.main.news.contract.News;
import com.kaiman.sports.main.news.PublicationViewModel;
import java.util.List;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class NewsPresenter extends BasePresenter<News.View> implements News.Presenter {
    private final LocalUserRepository localUserRepository;
    private NewsDataSource newsRepository;

    public NewsPresenter(@NonNull NewsDataSource newsRepository, LocalUserRepository localUserRepository) {
        this.newsRepository = newsRepository;
        this.localUserRepository = localUserRepository;
    }

    @Override
    public void initialize(News.View view) {
        super.initialize(view);
        if (localUserRepository.getLoggedUser().canCreatePublications()){
            view.showCreatePublicationButton();
        }
    }

    private void fetchNews() {
        view.showProgress();
        addSubscription(newsRepository.getNews().subscribeWith(new DisposableObserver<List<PublicationViewModel>>(view) {
            @Override
            public void onNext(List<PublicationViewModel> publicationViewModels) {
                if (publicationViewModels.isEmpty()) {
                    view.showNoPublicationsFound();
                } else {
                    view.setupList(publicationViewModels);
                }
            }
        }));
    }

    @Override
    public void onRetryClicked() {
        fetchNews();
    }

    @Override
    public void refreshContent() {
        fetchNews();
    }

    @Override
    public void onPublicationLikeClicked(final int position, PublicationViewModel item, final boolean liked) {
        clearSubscriptions();
        if (liked) {
            addSubscription(newsRepository.postReaction(item.id).subscribeWith(new DisposableObserver<String>(view) {
                @Override
                public void onNext(String newReactionId) {
                    view.updateReactionId(position, newReactionId);
                }

                @Override
                public void onError(Throwable e) {
                    view.showReactionErrorMessage();
                    view.revertReaction(position, liked);
                }
            }));
        } else {
            addSubscription(newsRepository.deleteReaction(item.reactionId).subscribeWith(new DisposableCompletableObserver(view) {
                @Override
                public void onComplete() {
                    view.updateReactionId(position, null);
                }

                @Override
                public void onError(Throwable e) {
                    view.showReactionErrorMessage();
                    view.revertReaction(position, liked);
                }
            }));
        }
    }

    @Override
    public void onPublicationClicked(PublicationViewModel item) {
        view.openPublicationComments(item);
    }

    @Override
    public void deletePublication(final PublicationViewModel item) {
        view.showProgress();
        newsRepository.deletePublication(item.id)
                .subscribe(new DisposableCompletableObserver(view) {
                    @Override
                    public void onComplete() {
                        view.hideProgress();
                        view.removePublication(item);
                    }
                });
    }
}
