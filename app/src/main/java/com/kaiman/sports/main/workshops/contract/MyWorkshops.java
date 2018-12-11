package com.kaiman.sports.main.workshops.contract;

import com.core.presentation.contract.BaseViewPresenter;
import com.core.presentation.contract.BaseView;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public interface MyWorkshops {
    interface View extends BaseView.RequestList<WorkshopViewModel> {
        void showNoWorkshopsPlaceholder();
    }

    interface Presenter extends BaseViewPresenter<View> {
        void onRetryClicked();
    }
}
