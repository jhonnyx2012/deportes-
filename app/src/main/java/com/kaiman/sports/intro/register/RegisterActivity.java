package com.kaiman.sports.intro.register;

import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import com.core.presentation.activity.BaseRequestActivity;
import com.core.presentation.contract.BaseView;
import com.core.util.AndroidUtils;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ActivityRegisterBinding;
import com.kaiman.sports.intro.login.LoginActivity;
import com.kaiman.sports.main.TabsActivity;

/**
 * Created by jhonnybarrios on 3/11/18
 */
public class RegisterActivity extends BaseRequestActivity<ActivityRegisterBinding> implements Register.View {

    Register.Presenter presenter;

    @Override protected void setEnterTransitionAnimations() {
        overridePendingTransition(com.core.R.anim.fade_in, com.core.R.anim.fade_out);
    }

    @Override protected void setExitTransitionAnimations() {
        overridePendingTransition(com.core.R.anim.fade_in, com.core.R.anim.fade_out);
    }

    @Override
    protected void initView() {
        presenter.initialize(this);

        binder.imageBack.setOnClickListener(onBackClickListener);
        binder.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                presenter.onRegisterClicked();
            }
        });
        binder.inputConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    AndroidUtils.hideSoftKeyboard(v);
                    presenter.onRegisterClicked();
                    return true;
                }
                return false;
            }
        });
        binder.video.view.playVideo(R.raw.video_intro);
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideRegisterPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void startLoginActivity() {
        startActivity(LoginActivity.class);
        finish();
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
    public String getConfirmPassword() {
        return binder.inputConfirmPassword.getText().toString().trim();
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
    public void showNotMatchingPasswordError() {
        binder.inputLayoutConfirmPassword.setError(getString(R.string.not_matching_pasword_error));
    }

    @Override
    public void clearErrorFromFields() {
        binder.inputLayoutPassword.setError(null);
        binder.inputLayoutEmail.setError(null);
        binder.inputLayoutConfirmPassword.setError(null);
    }

    @Override
    public void showRegisterSuccessMessage() {
        Toast.makeText(this, R.string.register_success_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void startTabsActivity() {
        startActivity(TabsActivity.class);
        ActivityCompat.finishAffinity(this);
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