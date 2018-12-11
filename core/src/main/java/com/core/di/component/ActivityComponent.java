package com.core.di.component;

/**
 * Created by jhonnybarrios on 01-09-17.
 */
import android.app.Activity;

public interface ActivityComponent<T extends Activity>{
    /**
     * Indica que T requiere inyección desde este componente
     * @param target T
     */
    void inject(T target);
}