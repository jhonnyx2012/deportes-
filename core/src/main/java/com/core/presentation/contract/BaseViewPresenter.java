package com.core.presentation.contract;

/**
 * Created by Jhonny Barrios.
 *
 * Base model for every presenter that will be connected to a fragment or activity
 *
 * The presenter will be the bridge between Activity or Fragment and an interactor.
 * Basically a presenter will communicate the results of background tasks (like a server request
 * or query to database) that are needed to affect the UI
 */
public interface BaseViewPresenter<T> {
    /**
     * This method will be executed after
     * [AppCompatActivity.onCreate] ()} in case presenter is attached to activity <br></br>
     * [Fragment.onCreateView] ()}  in case presenter is attached to fragment
     */
    void initialize(T view);

    /**
     * This method should be called then the view is being destroyed
     */
    void onDestroy();
}
