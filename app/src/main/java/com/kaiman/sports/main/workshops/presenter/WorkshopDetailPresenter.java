package com.kaiman.sports.main.workshops.presenter;

import android.support.annotation.NonNull;
import com.core.presentation.BasePresenter;
import com.kaiman.sports.R;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.user.local.LocalUserRepository;
import com.kaiman.sports.data.repository.workshop.WorkshopDataSource;
import com.kaiman.sports.main.workshops.contract.WorkshopDetail;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class WorkshopDetailPresenter extends BasePresenter<WorkshopDetail.View> implements WorkshopDetail.Presenter {
    private final LocalUserRepository localUserRepository;
    private WorkshopDataSource workshopRepository;
    private String workshopId;
    private WorkshopViewModel workshop;

    public WorkshopDetailPresenter(@NonNull WorkshopDataSource workshopRepository, LocalUserRepository localUserRepository) {
        this.workshopRepository = workshopRepository;
        this.localUserRepository = localUserRepository;
    }

    @Override
    public void initialize(WorkshopDetail.View view) {
        super.initialize(view);
        fetchWorkshopDetail();
        if (localUserRepository.getLoggedUser().canSubscribe()) {
            checkIfImSubscribed();
        } else {
            view.hideSubscribeButton();
        }

    }

    private void checkIfImSubscribed() {
        addSubscription(workshopRepository.checkIfImSubscribed(workshopId).subscribeWith(new DisposableObserver<Boolean>(view) {
            @Override
            public void onNext(Boolean imSubscribed) {
                if (!imSubscribed) {
                    view.showSubscribeButton();
                } else {
                    view.showAlreadySubscribed();
                }
            }
        }));
    }

    private void fetchWorkshopDetail() {
        view.showProgress();
        addSubscription(workshopRepository.getWorkshop(workshopId).subscribeWith(new DisposableObserver<WorkshopViewModel>(view) {
            @Override
            public void onNext(WorkshopViewModel result) {
                workshop = result;
                view.setupDetails(result);
            }
        }));
    }

    @Override
    public void onRetryClicked() {
        fetchWorkshopDetail();
        checkIfImSubscribed();
    }

    @Override
    public void setWorkshopId(String id) {
        workshopId = id;
    }

    @Override
    public void subscribeToWorkshop() {
        if (!localUserRepository.getLoggedUser().hasFilledAllPersonalData()) {
            view.showMustCompletePersonalDataMessage();
            view.openEditUser();
            return;
        }
        view.showProgress();
        addSubscription(workshopRepository.subscribeToWorkshop(workshopId).subscribeWith(new DisposableObserver<Boolean>(view) {
            @Override
            public void onNext(Boolean result) {
                view.hideProgress();
                view.hideSubscribeButton();
                view.showAlreadySubscribed();
                view.showSuccessSubscriptionMessage();
            }

            @Override
            protected void onRequestError(String errorMessage) {
                view.showMessage(R.string.ups, errorMessage);
            }

            @Override
            protected void onConnectionError() {
                view.showConnectionErrorDialog();
            }
        }));
    }

    @Override
    public void onHowToGetThereClicked() {
        if (workshop != null && workshop.address != null && !workshop.address.isEmpty()) {
            view.openAddress(workshop.address);
        }
    }
}