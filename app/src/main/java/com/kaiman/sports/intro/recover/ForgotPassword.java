package com.kaiman.sports.intro.recover;

import com.core.presentation.contract.BaseView;
import com.core.presentation.contract.BaseViewPresenter;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public interface ForgotPassword {
    interface View extends BaseView.Request {

        String getEmail();

        void showEmailEmptyError();

        void showInvalidEmailError();

        void clearErrorFromFields();

        void showEmailSentMessage();
    }

    interface Presenter extends BaseViewPresenter<View> {
        void onForgotClicked();
    }
}