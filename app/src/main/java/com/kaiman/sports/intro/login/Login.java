package com.kaiman.sports.intro.login;

import com.core.presentation.contract.BaseViewPresenter;
import com.core.presentation.contract.BaseView;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public interface Login {
    interface View extends BaseView.Request {

        void startTabsActivity();

        String getEmail();

        String getPassword();

        void showEmailEmptyError();

        void showPasswordEmptyError();

        void showInvalidEmailError();

        void showInvalidPasswordError();

        void clearErrorFromFields();
    }

    interface Presenter extends BaseViewPresenter<View> {
        void onLoginClicked();
    }
}
