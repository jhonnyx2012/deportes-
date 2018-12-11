package com.core.di.component;

/**
 * Created by jhonnybarrios on 01-09-17.
 */
import android.support.v4.app.Fragment;

public interface FragmentComponent<T extends Fragment>{
    /**
     * Indica que T requiere inyección desde este componente
     * @param target T
     */
    void inject(T target);
}