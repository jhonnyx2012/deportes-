package com.kaiman.sports.intro.recover;

import android.support.annotation.NonNull;

import com.core.presentation.BasePresenter;
import com.core.validation.EmailValidator;
import com.kaiman.sports.data.remote.DisposableCompletableObserver;
import com.kaiman.sports.data.repository.user.UserDataSource;
import com.kaiman.sports.utils.Utils;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class ForgotPasswordPresenter extends BasePresenter<ForgotPassword.View> implements ForgotPassword.Presenter {
    private final EmailValidator emailValidator;
    private UserDataSource userRepository;

    public ForgotPasswordPresenter(@NonNull UserDataSource userRepository) {
        this.userRepository = userRepository;
        this.emailValidator = new EmailValidator();
    }

    private void doRecover(String email) {
        view.showProgress();
        addSubscription(userRepository.resetPassword(email).subscribeWith(new DisposableCompletableObserver(view) {
            @Override
            public void onComplete() {
                view.showEmailSentMessage();
            }
        }));
    }

    @Override
    public void onForgotClicked() {
        view.clearErrorFromFields();
        String email = view.getEmail();

        if (!Utils.nonNullOrEmpty(email)) {
            view.showEmailEmptyError();

        } else if (!emailValidator.isValid(email)) {
            view.showInvalidEmailError();

        } else {
            doRecover(email);
        }
    }
}
