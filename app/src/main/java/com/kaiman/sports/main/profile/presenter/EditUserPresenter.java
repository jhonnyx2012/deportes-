package com.kaiman.sports.main.profile.presenter;

import android.support.annotation.NonNull;

import com.core.presentation.BasePresenter;
import com.core.validation.PasswordValidator;
import com.core.validation.RutValidator;
import com.kaiman.sports.data.entity.User;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.user.UserDataSource;
import com.kaiman.sports.intro.register.RegisterPresenter;
import com.kaiman.sports.main.profile.contract.EditUser;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class EditUserPresenter extends BasePresenter<EditUser.View> implements EditUser.Presenter {
    private UserDataSource userRepository;

    public EditUserPresenter(@NonNull UserDataSource userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(EditUser.View view) {
        super.initialize(view);
        view.setupUser(userRepository.getLoggedUser());
    }

    @Override
    public void attemptSave(String rut, String name, String lastName, String address, String password) {
        RutValidator rutValidator = new RutValidator();
        PasswordValidator passwordValidator = new PasswordValidator(RegisterPresenter.PASSWORD_MIN_CHARACTERS);

        view.clearErrorFromFields();

        rut = rut.isEmpty()? null : rut;
        name = name.isEmpty()? null : name;
        lastName = lastName.isEmpty()? null : lastName;
        address = address.isEmpty()? null : address;
        password = password.isEmpty()? null : password;

        if (rut != null && !rutValidator.isValid(rut)) {
            view.showInvalidRutError();

        } else if (password != null && !passwordValidator.isValid(password)) {
            view.showInvalidPasswordError();

        }else {
            doSave(rut, name, lastName, address, password);
        }
    }

    private void doSave(String rut, String name, String lastName, String address, String password) {
        view.showProgress();
        addSubscription(userRepository.editUser(rut, name, lastName, address, password)
                .subscribeWith(new DisposableObserver<User>(view) {
                    @Override
                    public void onNext(User user) {
                        view.hideProgress();
                        view.showSuccessEditMessage();
                        view.goBack();
                    }
                }));
    }
}