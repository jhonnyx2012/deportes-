package com.kaiman.sports.data.remote;

import android.support.annotation.NonNull;

import com.core.data.remote.SchedulerProvider;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jhonnybarrios on 3/13/18.
 */

public class RuntimeSchedulerProvider implements SchedulerProvider {
    @NonNull
    @Override
    public Scheduler observer() {
        return AndroidSchedulers.mainThread();
    }

    @NonNull
    @Override
    public Scheduler subscriber() {
        return Schedulers.io();
    }
}
