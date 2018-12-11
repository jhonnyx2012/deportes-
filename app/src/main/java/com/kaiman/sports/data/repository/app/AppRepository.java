package com.kaiman.sports.data.repository.app;

import android.support.annotation.NonNull;

import com.core.data.remote.SchedulerProvider;
import com.kaiman.sports.data.entity.MobileVersion;
import com.kaiman.sports.data.remote.ApiService;
import com.kaiman.sports.data.remote.response.ApiResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class AppRepository implements AppDataSource {

    private final ApiService apiService;
    private final SchedulerProvider schedulerProvider;

    public AppRepository(@NonNull ApiService apiService,
                         @NonNull SchedulerProvider schedulerProvider) {
        this.apiService = apiService;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Observable<String> fetchAppVersion() {
        return apiService.fetchAppVersion()
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<List<MobileVersion>>, String>() {
                    @Override
                    public String apply(ApiResponse<List<MobileVersion>> listApiResponse) throws Exception {
                        return listApiResponse.data.get(0).version;
                    }
                });
    }
}