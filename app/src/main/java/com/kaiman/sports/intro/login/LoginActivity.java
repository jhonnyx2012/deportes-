package com.kaiman.sports.intro.login;

import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.core.presentation.activity.BaseRequestActivity;
import com.core.presentation.contract.BaseView;
import com.core.util.AndroidUtils;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ActivityLoginBinding;
import com.kaiman.sports.intro.recover.ForgotPasswordActivity;
import com.kaiman.sports.main.TabsActivity;
import com.kaiman.sports.utils.Utils;

/**
 * Created by jhonnybarrios on 3/11/18
 */

public class LoginActivity extends BaseRequestActivity<ActivityLoginBinding> implements Login.View {

    private Login.Presenter presenter;

    @Override protected void setEnterTransitionAnimations() {
        overridePendingTransition(com.core.R.anim.fade_in, com.core.R.anim.fade_out);
    }

    @Override protected void setExitTransitionAnimations() {
        overridePendingTransition(com.core.R.anim.fade_in, com.core.R.anim.fade_out);
    }

    @Override
    protected void initView() {
        Utils.checkAppVersion(this);

        presenter.initialize(this);

        binder.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                presenter.onLoginClicked();
            }
        });

        binder.textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ForgotPasswordActivity.class);
            }
        });

        binder.inputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    AndroidUtils.hideSoftKeyboard(v);
                    presenter.onLoginClicked();
                    return true;
                }
                return false;
            }
        });
        binder.video.view.playVideo(R.raw.video_intro);
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideLoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void startTabsActivity() {
        startActivity(TabsActivity.class);
        ActivityCompat.finishAffinity(this);
    }

    @Override
    public String getEmail() {
        return binder.inputEmail.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return binder.inputPassword.getText().toString().trim();
    }

    @Override
    public void showEmailEmptyError() {
        binder.inputLayoutEmail.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showPasswordEmptyError() {
        binder.inputLayoutPassword.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showInvalidEmailError() {
        binder.inputLayoutEmail.setError(getString(R.string.invalid_email_error));
    }

    @Override
    public void showInvalidPasswordError() {
        binder.inputLayoutPassword.setError(getString(R.string.invalid_password_error));
    }

    @Override
    public void clearErrorFromFields() {
        binder.inputLayoutPassword.setError(null);
        binder.inputLayoutEmail.setError(null);
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
