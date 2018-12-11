package com.kaiman.sports.main.workshops.contract;

import com.core.presentation.contract.BaseViewPresenter;
import com.core.presentation.contract.BaseView;
import com.kaiman.sports.main.workshops.model.DisciplineViewModel;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;

import java.util.List;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public interface WorkshopList {
    interface View extends BaseView.RequestList<WorkshopViewModel> {
        void openLessons(WorkshopViewModel item);

        void setupDisciplines(List<DisciplineViewModel> result);

        void showNoResultsPlaceholder();

        void openDetails(WorkshopViewModel item);
    }

    interface Presenter extends BaseViewPresenter<View> {
        void onItemClicked(WorkshopViewModel item);
        void setWorkshopTypeId(String typeId);

        void onDisciplineClicked(DisciplineViewModel item);

        void onRetryClicked(boolean shouldReloadDisciplines);
    }
}