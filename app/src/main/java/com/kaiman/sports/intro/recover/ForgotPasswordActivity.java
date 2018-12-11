package com.kaiman.sports.intro.recover;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.core.presentation.activity.BaseRequestActivity;
import com.core.presentation.contract.BaseView;
import com.core.util.AndroidUtils;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ActivityForgotPasswordBinding;

/**
 * Created by jhonnybarrios on 3/11/18
 */

public class ForgotPasswordActivity extends BaseRequestActivity<ActivityForgotPasswordBinding> implements ForgotPassword.View {

    private ForgotPassword.Presenter presenter;

    @Override protected void setEnterTransitionAnimations() {
        overridePendingTransition(com.core.R.anim.fade_in, com.core.R.anim.fade_out);
    }

    @Override protected void setExitTransitionAnimations() {
        overridePendingTransition(com.core.R.anim.fade_in, com.core.R.anim.fade_out);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void initView() {
        presenter.initialize(this);

        binder.buttonRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                AndroidUtils.hideSoftKeyboard(binder.inputEmail);
                presenter.onForgotClicked();
            }
        });

        binder.video.view.playVideo(R.raw.video_intro);
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideRecoverPasswordPresenter();
    }

    @Override
    public String getEmail() {
        return binder.inputEmail.getText().toString().trim();
    }

    @Override
    public void showEmailEmptyError() {
        binder.inputLayoutEmail.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showInvalidEmailError() {
        binder.inputLayoutEmail.setError(getString(R.string.invalid_email_error));
    }

    @Override
    public void clearErrorFromFields() {
        binder.inputLayoutEmail.setError(null);
    }

    @Override
    public void showEmailSentMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setCancelable(false)
                .setTitle(getString(R.string.done))
                .setMessage(getString(R.string.email_sent_message))
                .setPositiveButton(com.core.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                }).create();
        dialog.show();
    }

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return Injection.provideIntroRequestViewHelper(binder.container);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}