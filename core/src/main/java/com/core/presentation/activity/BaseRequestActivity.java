package com.core.presentation.activity;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.core.presentation.contract.BaseView;

/**
 * Created by jhonnybarrios on 10/23/17
 */

public abstract class BaseRequestActivity<BINDER extends ViewDataBinding> extends BaseActivity<BINDER> implements BaseView.Request {

    private BaseView.Request requestViewHelper;

    @Override
    protected void setupToolbar() {
        this.requestViewHelper = provideRequestViewHelper();
        super.setupToolbar();
    }

    protected abstract BaseView.Request provideRequestViewHelper();

    @Override
    public void showProgress() {
        requestViewHelper.showProgress();
    }

    @Override
    public void hideProgress() {
        requestViewHelper.hideProgress();
    }

    @Override
    public void showConnectionError(View.OnClickListener onClickListener) {
        requestViewHelper.showConnectionError(onClickListener);
    }

    @Override
    public void showRequestError(int imageRes, String description, View.OnClickListener onClickListener) {
        requestViewHelper.showRequestError(imageRes, description, onClickListener);
    }

    @Override
    public void showRequestError(String message) {
        requestViewHelper.showRequestError(message);
    }

    @Override
    public void showMessage(int title, String message) {
        requestViewHelper.showMessage(title, message);
    }

    @Override
    public void showPlaceholder(int imageRes, int textRes, View.OnClickListener onClickListener) {
        requestViewHelper.showPlaceholder(imageRes, textRes, onClickListener);
    }

    @Override
    public void onRetryLoadingClicked() {

    }
}