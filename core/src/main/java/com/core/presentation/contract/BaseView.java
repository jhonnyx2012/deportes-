package com.core.presentation.contract;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;

import java.util.List;

/**
 * Created by jhonnybarrios on 3/21/18
 */

public interface BaseView {

    interface Request {
        void showProgress();
        void hideProgress();
        void showConnectionError(View.OnClickListener onClickListener);
        void showRequestError(int imageRes, String description, View.OnClickListener onClickListener);
        void showRequestError(String message);
        void showMessage(@StringRes int title, String message);
        void showPlaceholder(@DrawableRes int imageRes, @StringRes int textRes, View.OnClickListener onClickListener);
        void onRetryLoadingClicked();
    }

    interface RequestList<MODEL> extends Request {
        void setupList(List<MODEL> items);
    }
}