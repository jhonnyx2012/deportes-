package com.core.presentation;

import android.support.annotation.CallSuper;
import com.core.presentation.contract.BaseViewPresenter;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<T> implements BaseViewPresenter<T> {

    protected T view;
    private CompositeDisposable compositeSubscription = new CompositeDisposable();

    protected boolean isViewAttached() {
        return view != null;
    }

    protected void addSubscription(Disposable disposable) {
        this.compositeSubscription.add(disposable);
    }

    protected void clearSubscriptions() {
        compositeSubscription.clear();
    }

    @Override
    @CallSuper
    public void initialize(T view) {
        this.view = view;
    }

    @Override
    @CallSuper
    public void onDestroy() {
        view = null;
        compositeSubscription.clear();
    }
}
