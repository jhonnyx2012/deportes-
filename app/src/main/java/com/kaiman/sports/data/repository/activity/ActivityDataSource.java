package com.kaiman.sports.data.repository.activity;

import com.kaiman.sports.main.activities.ActivityViewModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public interface ActivityDataSource {
    Observable<List<ActivityViewModel>> getActivities();
}