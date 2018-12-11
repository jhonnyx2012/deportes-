package com.core.data.remote;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * Created by jhonnybarrios on 3/13/18
 */

public interface SchedulerProvider {
    @NonNull
    Scheduler observer();

    @NonNull
    Scheduler subscriber();
}
