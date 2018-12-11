package com.kaiman.sports.data.remote;

import android.view.View;

import com.core.presentation.contract.BaseView;
import com.google.gson.Gson;
import com.kaiman.sports.data.remote.response.ApiResponse;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

/**
 * This class provide a better error handling and avoid to override the onComplete
 * method on every instance of {@link io.reactivex.observers.DisposableObserver<T>}
 */
public abstract class DisposableCompletableObserver extends io.reactivex.observers.DisposableCompletableObserver {
    private final BaseView.Request view;

    public DisposableCompletableObserver(BaseView.Request view) {
        this.view = view;
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (isRequestError(e)) {
            onRequestError(getErrorMessage((HttpException) e));
        } else if (isConnectionError(e)) {
            onConnectionError();
        } else onRequestError(e.getMessage());
    }

    private String getErrorMessage(HttpException e) {
        try {
            String errorBody = e.response().errorBody().string();
            ApiResponse<Void> response = new Gson().fromJson(errorBody, ApiResponse.class);
            if (response.error != null) {
                return response.error;
            }
            return response.errors.get(0);
        } catch (Exception e1) {
            e1.printStackTrace();
            return e.message();
        }
    }

    protected void onRequestError(String errorMessage){
        view.showRequestError(com.core.R.drawable.ic_cloud_error_request,errorMessage, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.onRetryLoadingClicked();
            }
        });
    }

    protected void onConnectionError() {
        view.showConnectionError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.onRetryLoadingClicked();
            }
        });
    }

    protected boolean isConnectionError(Throwable e){
        return e instanceof SocketTimeoutException || e instanceof IOException;
    }

    protected boolean isRequestError(Throwable e){
        return e instanceof HttpException;
    }
}
