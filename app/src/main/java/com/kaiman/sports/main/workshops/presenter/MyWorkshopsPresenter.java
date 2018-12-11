package com.kaiman.sports.main.workshops.presenter;

import android.support.annotation.NonNull;
import com.core.presentation.BasePresenter;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.workshop.WorkshopDataSource;
import com.kaiman.sports.main.workshops.contract.MyWorkshops;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;
import java.util.List;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class MyWorkshopsPresenter extends BasePresenter<MyWorkshops.View> implements MyWorkshops.Presenter {
    private WorkshopDataSource workshopRepository;

    public MyWorkshopsPresenter(@NonNull WorkshopDataSource workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    @Override
    public void initialize(MyWorkshops.View view) {
        super.initialize(view);
        fetchWorkshops();
    }

    private void fetchWorkshops() {
        view.showProgress();
        addSubscription(workshopRepository.getMyWorkshops().subscribeWith(new DisposableObserver<List<WorkshopViewModel>>(view) {
            @Override
            public void onNext(List<WorkshopViewModel> result) {
                if (!result.isEmpty()) {
                    view.setupList(result);
                } else {
                    view.showNoWorkshopsPlaceholder();
                }
            }
        }));
    }

    @Override
    public void onRetryClicked() {
        fetchWorkshops();
    }
}
