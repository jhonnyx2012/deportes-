package com.kaiman.sports.intro.login;

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

public class LoginPresenter extends BasePresenter<Login.View> implements Login.Presenter {
    private static final int PASSWORD_MIN_CHARACTERS = 6;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private UserDataSource userRepository;

    public LoginPresenter(@NonNull UserDataSource userRepository) {
        this.userRepository = userRepository;
        this.emailValidator = new EmailValidator();
        this.passwordValidator = new PasswordValidator(PASSWORD_MIN_CHARACTERS);
    }

    @Override public void onLoginClicked() {
        view.clearErrorFromFields();
        String email = view.getEmail();
        String password = view.getPassword();

        if (!Utils.nonNullOrEmpty(email)) {
            view.showEmailEmptyError();

        } else if (!Utils.nonNullOrEmpty(password)) {
            view.showPasswordEmptyError();

        } else if (!emailValidator.isValid(email)) {
            view.showInvalidEmailError();

        } else if (!passwordValidator.isValid(password)) {
            view.showInvalidPasswordError();

        } else {
            doLogin(email, password);
        }
    }

    private void doLogin(String email, String password) {
        view.showProgress();
        addSubscription(userRepository.login(email, password).subscribeWith(new DisposableObserver<User>(view) {
            @Override
            public void onNext(User user) {
                view.startTabsActivity();
            }
        }));
    }
}
