package com.kaiman.sports.main.activities;

import com.core.presentation.contract.BaseViewPresenter;
import com.core.presentation.contract.BaseView;
/**
 * Created by jhonnybarrios on 3/15/18
 */

public interface Activities {
    interface View extends BaseView.RequestList<ActivityViewModel> {}
    interface Presenter extends BaseViewPresenter<View> {
        void onRetryClicked();
    }
}