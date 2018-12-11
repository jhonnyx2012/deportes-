package com.kaiman.sports.main.profile.contract;

import com.core.presentation.contract.BaseView;
import com.core.presentation.contract.BaseViewPresenter;
import com.kaiman.sports.data.entity.User;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public interface EditUser {
    interface View extends BaseView.Request {
        void setupUser(User user);

        void clearErrorFromFields();

        String getRut();
        String getName();
        String getLastName();
        String getAddress();
        String getPassword();

        void showSuccessEditMessage();

        void showRutEmptyError();

        void showNameEmptyError();

        void showAddressEmptyError();

        void showPasswordEmptyError();

        void showLastNameEmptyError();

        void showInvalidRutError();

        void showInvalidPasswordError();

        void goBack();
    }

    interface Presenter extends BaseViewPresenter<View> {
        void attemptSave(String rut, String name, String lastName, String address, String password);
    }
}