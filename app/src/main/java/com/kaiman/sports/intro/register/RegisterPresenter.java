package com.kaiman.sports.intro.register;

import android.support.annotation.NonNull;
import com.core.presentation.BasePresenter;
import com.core.validation.EmailValidator;
import com.core.validation.PasswordValidator;
import com.kaiman.sports.data.entity.User;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.user.UserDataSource;
import com.kaiman.sports.utils.Utils;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class RegisterPresenter extends BasePresenter<Register.View> implements Register.Presenter {
    public static final int PASSWORD_MIN_CHARACTERS = 6;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private UserDataSource userRepository;

    public RegisterPresenter(@NonNull UserDataSource userRepository) {
        this.userRepository = userRepository;
        this.emailValidator = new EmailValidator();
        this.passwordValidator = new PasswordValidator(PASSWORD_MIN_CHARACTERS);
    }

    @Override
    public void onRegisterClicked() {
        view.clearErrorFromFields();
        String email = view.getEmail();
        String password = view.getPassword();
        String confirmPassword = view.getConfirmPassword();

        if (!Utils.nonNullOrEmpty(email)) {
            view.showEmailEmptyError();

        } else if (!Utils.nonNullOrEmpty(password)) {
            view.showPasswordEmptyError();

        } else if (!emailValidator.isValid(email)) {
            view.showInvalidEmailError();

        } else if (!passwordValidator.isValid(password)) {
            view.showInvalidPasswordError();

        } else if (!confirmPassword.equals(password)) {
            view.showNotMatchingPasswordError();

        } else {
            doRegister(email, password, confirmPassword);
        }
    }

    private void doRegister(String email, String password, String confirmPassword) {
        view.showProgress();
        addSubscription(userRepository.register(email, password, confirmPassword)
                .subscribeWith(new DisposableObserver<User>(view) {
                    @Override
                    public void onNext(User user) {
                        //view.hideProgress();
                        //view.showRegisterSuccessMessage();
                        //view.startLoginActivity();
                        //TODO: volver a activar cuando el mail de activación funcione
                        view.startTabsActivity();
                    }
                }));
    }
}
