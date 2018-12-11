package com.core.presentation.fragment;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.core.presentation.contract.BaseView;

/**
 * Created by jhonnybarrios on 10/23/17
 */

public abstract class BaseRequestFragment<BINDER extends ViewDataBinding> extends BaseFragment<BINDER> implements BaseView.Request {

    protected BaseView.Request requestViewHelper;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.requestViewHelper = provideRequestViewHelper();
        super.onViewCreated(view, savedInstanceState);
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
    public void showRequestError(String message) {
        requestViewHelper.hideProgress();
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
    public void showRequestError(int imageRes, String description, View.OnClickListener onClickListener) {
        requestViewHelper.showRequestError(imageRes, description, onClickListener);
    }

    @Override
    public void onRetryLoadingClicked() {

    }
}