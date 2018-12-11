package com.kaiman.sports.data.repository.app;

import com.kaiman.sports.data.entity.DeviceRegistration;
import com.kaiman.sports.data.entity.User;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public interface AppDataSource {
    Observable<String> fetchAppVersion();
}