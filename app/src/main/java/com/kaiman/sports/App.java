package com.kaiman.sports;

import android.app.Application;

/**
 * Created by jhonnybarrios on 3/12/18.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Injection.init(this);
    }
}