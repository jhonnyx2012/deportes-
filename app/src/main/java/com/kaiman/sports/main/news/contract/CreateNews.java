package com.kaiman.sports.main.news.contract;

import com.core.presentation.contract.BaseView;
import com.core.presentation.contract.BaseViewPresenter;

import java.io.File;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public interface CreateNews {
    interface View extends BaseView.Request {

        void clearErrorFromFields();

        void showEmptyDescriptionError();

        void showSuccessCreationMessage();

        void goBack();

        void showImage(File file);

        void showNoImageTakedError();
    }

    interface Presenter extends BaseViewPresenter<View> {

        void attemptPost(String description);

        void setImageToUpload(File file);
    }
}
