package com.kaiman.sports.main.profile;

import android.view.View;
import com.core.presentation.activity.BaseRequestActivity;
import com.core.presentation.contract.BaseView;
import com.core.util.AndroidUtils;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.data.entity.User;
import com.kaiman.sports.databinding.ActivityEditUserBinding;
import com.kaiman.sports.main.profile.contract.EditUser;

/**
 * Created by jhonnybarrios on 4/1/18
 */

public class EditUserActivity extends BaseRequestActivity<ActivityEditUserBinding> implements EditUser.View {

    private EditUser.Presenter presenter;

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return Injection.provideIntroRequestViewHelper(binder.root);
    }

    @Override
    protected void initView() {
        setTitle(R.string.title_edit_user);
        showUpButton(true);
        setupListeners();
        presenter.initialize(this);
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideEditUserPresenter();
    }

    private void setupListeners() {
        binder.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.hideSoftKeyboard(v);
                presenter.attemptSave(getRut(),getName(), getLastName(), getAddress(), getPassword());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_user;
    }

    @Override
    public void setupUser(User user) {
        binder.inputName.setText(user.first_name);
        binder.inputLastName.setText(user.last_name);
        binder.inputRut.setText(user.rut);
        binder.inputAddress.setText(user.address);
    }

    @Override
    public void clearErrorFromFields() {
        binder.inputLayoutPassword.setError(null);
        binder.inputLayoutLastName.setError(null);
        binder.inputLayoutName.setError(null);
        binder.inputLayoutAddress.setError(null);
        binder.inputLayoutRut.setError(null);
    }

    @Override
    public String getRut() {
        return binder.inputRut.getText().toString().trim();
    }

    @Override
    public String getName() {
        return binder.inputName.getText().toString().trim();
    }

    @Override
    public String getLastName() {
        return binder.inputLastName.getText().toString().trim();
    }

    @Override
    public String getAddress() {
        return binder.inputAddress.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return binder.inputPassword.getText().toString().trim();
    }

    @Override
    public void showSuccessEditMessage() {
        showMessage(R.string.great, getString(R.string.successfull_user_edition));
    }

    @Override
    public void showRutEmptyError() {
        binder.inputLayoutRut.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showNameEmptyError() {
        binder.inputLastName.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showAddressEmptyError() {
        binder.inputLayoutAddress.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showPasswordEmptyError() {
        binder.inputLayoutPassword.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showLastNameEmptyError() {
        binder.inputLayoutLastName.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showInvalidRutError() {
        binder.inputLayoutRut.setError(getString(R.string.invalid_rut_error));
    }

    @Override
    public void showInvalidPasswordError() {
        binder.inputLayoutPassword.setError(getString(R.string.invalid_password_error));
    }

    @Override
    public void goBack() {
        onBackPressed();
    }
}