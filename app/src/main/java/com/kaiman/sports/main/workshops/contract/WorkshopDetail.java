package com.kaiman.sports.main.workshops.contract;

import com.core.presentation.contract.BaseView;
import com.core.presentation.contract.BaseViewPresenter;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public interface WorkshopDetail {
    interface View extends BaseView.Request {
        void openAddress(String address);

        void setupDetails(WorkshopViewModel result);
        void showSubscribeButton();
        void hideSubscribeButton();
        void showConnectionErrorDialog();

        void showSuccessSubscriptionMessage();

        void showAlreadySubscribed();

        void showMustCompletePersonalDataMessage();

        void openEditUser();
    }

    interface Presenter extends BaseViewPresenter<View> {
        void onRetryClicked();

        void setWorkshopId(String id);

        void subscribeToWorkshop();

        void onHowToGetThereClicked();
    }
}