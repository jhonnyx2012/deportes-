package com.kaiman.sports.main.workshops.presenter;

import android.support.annotation.NonNull;
import com.core.presentation.BasePresenter;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.workshop.WorkshopDataSource;
import com.kaiman.sports.main.workshops.contract.WorkshopList;
import com.kaiman.sports.main.workshops.model.DisciplineViewModel;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;
import java.util.List;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class WorkshopListPresenter extends BasePresenter<WorkshopList.View> implements WorkshopList.Presenter {
    private WorkshopDataSource workshopRepository;
    private String workshopTypeId;
    private String lastDisciplineFetched;

    public WorkshopListPresenter(@NonNull WorkshopDataSource workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    @Override
    public void initialize(WorkshopList.View view) {
        super.initialize(view);
        fetchDisciplines();
    }

    private void fetchDisciplines() {
        addSubscription(workshopRepository.getDisciplines(workshopTypeId).subscribeWith(new DisposableObserver<List<DisciplineViewModel>>(view) {
            @Override
            public void onNext(List<DisciplineViewModel> result) {
                view.setupDisciplines(result);
            }
        }));
    }

    private void fetchWorkshops(String workshopTypeId, String disciplineId) {
        view.showProgress();
        lastDisciplineFetched = disciplineId;
        addSubscription(workshopRepository.getWorkshops(workshopTypeId, disciplineId)
                .subscribeWith(new DisposableObserver<List<WorkshopViewModel>>(view) {
                    @Override
                    public void onNext(List<WorkshopViewModel> result) {
                        if (result.isEmpty()) {
                            view.showNoResultsPlaceholder();
                        } else {
                            view.setupList(result);
                        }
                    }
                }));
    }

    @Override
    public void onItemClicked(WorkshopViewModel item) {
        view.openDetails(item);
    }

    @Override
    public void setWorkshopTypeId(String typeId) {
        this.workshopTypeId = typeId;
    }

    @Override
    public void onDisciplineClicked(DisciplineViewModel item) {
        fetchWorkshops(workshopTypeId, item.id);
    }

    @Override
    public void onRetryClicked(boolean shouldReloadDisciplines) {
        if (shouldReloadDisciplines) {
            fetchDisciplines();
        }
        fetchWorkshops(workshopTypeId, lastDisciplineFetched);
    }
}