package com.kaiman.sports.main.news.contract;

import com.core.presentation.contract.BaseView;
import com.core.presentation.contract.BaseViewPresenter;
import com.kaiman.sports.main.news.CommentViewModel;

import java.util.List;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public interface Comments {

    String NEWS_ID_KEY = "news_id";

    interface View extends BaseView.Request {
        void setupList(List<CommentViewModel> items);

        void showNoCommentsFound();

        void removeCommentByPosition(int position);

        void showDeleteErrorMessage();

        void addComment(CommentViewModel commentViewModel);

        void showConfirmDeleteCommentDialog(int position, CommentViewModel item);

        void clearInput();
    }

    interface Presenter extends BaseViewPresenter<View> {
        void refreshContent();

        void onRetryClicked();

        void onDeleteCommentClicked(int position, CommentViewModel item);

        void onSendClicked(String comment);

        void deleteComment(int position, CommentViewModel item);
    }
}
