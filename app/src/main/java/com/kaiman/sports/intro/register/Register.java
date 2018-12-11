package com.kaiman.sports.intro.register;

import com.core.presentation.contract.BaseViewPresenter;
import com.core.presentation.contract.BaseView;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public interface Register {
    interface View extends BaseView.Request {

        void startLoginActivity();

        String getEmail();

        String getPassword();

        String getConfirmPassword();

        void showEmailEmptyError();

        void showPasswordEmptyError();

        void showInvalidEmailError();

        void showInvalidPasswordError();

        void showNotMatchingPasswordError();

        void clearErrorFromFields();

        void showRegisterSuccessMessage();

        void startTabsActivity();
    }

    interface Presenter extends BaseViewPresenter<View> {

        void onRegisterClicked();
    }
}
