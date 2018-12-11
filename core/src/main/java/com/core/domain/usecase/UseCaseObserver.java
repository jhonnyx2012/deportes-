package com.core.domain.usecase;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by jhonnybarrios on 10/23/17.
 */

public abstract class UseCaseObserver<T> extends DisposableObserver<T> {
    @Override public void onNext(T value) {}
    @Override public void onError(Throwable e) {}
    @Override public void onComplete() {}
}