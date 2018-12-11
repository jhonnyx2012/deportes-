package com.kaiman.sports.main.news.contract;

import com.core.presentation.contract.BaseViewPresenter;
import com.core.presentation.contract.BaseView;
import com.kaiman.sports.main.news.PublicationViewModel;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public interface News {
    interface View extends BaseView.RequestList<PublicationViewModel> {
        void showNoPublicationsFound();

        void showCreatePublicationButton();

        void updateReactionId(int position, String newReactionId);

        void showReactionErrorMessage();

        void revertReaction(int position, boolean liked);

        void openPublicationComments(PublicationViewModel item);

        void removePublication(PublicationViewModel item);
    }

    interface Presenter extends BaseViewPresenter<View> {
        void onRetryClicked();

        void refreshContent();

        void onPublicationLikeClicked(int position, PublicationViewModel item, boolean liked);

        void onPublicationClicked(PublicationViewModel item);

        void deletePublication(PublicationViewModel item);
    }
}
