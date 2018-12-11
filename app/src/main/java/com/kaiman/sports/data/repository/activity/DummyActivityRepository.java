package com.kaiman.sports.data.repository.activity;

import com.kaiman.sports.main.activities.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class DummyActivityRepository implements ActivityDataSource {

    @Override
    public Observable<List<ActivityViewModel>> getActivities() {
        List<ActivityViewModel> result = new ArrayList<>();
        ActivityViewModel item = new ActivityViewModel();
        //TODO: rellenar informacion
        result.add(item);
        result.add(item);
        result.add(item);
        result.add(item);
        result.add(item);
        result.add(item);
        result.add(item);
        return Observable.just(result);
    }
}