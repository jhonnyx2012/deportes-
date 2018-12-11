package com.kaiman.sports.main.workshops.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.core.presentation.adapter.OnItemClickListener;
import com.core.presentation.contract.BaseView;
import com.core.presentation.fragment.BaseRequestFragment;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.FragmentWorkshopListBinding;
import com.kaiman.sports.main.workshops.adapter.DisciplineSpinnerAdapter;
import com.kaiman.sports.main.workshops.adapter.WorkshopAdapter;
import com.kaiman.sports.main.workshops.contract.WorkshopList;
import com.kaiman.sports.main.workshops.model.DisciplineViewModel;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkshopListFragment extends BaseRequestFragment<FragmentWorkshopListBinding> implements WorkshopList.View {

    private static final String WORKSHOP_TYPE_ID = "workshop-type-id";
    WorkshopList.Presenter presenter;
    private DisciplineSpinnerAdapter spinnerAdapter;
    private WorkshopAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_workshop_list;
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideRecreativeWorkshopsPresenter();
    }

    @Override
    protected void initView() {
        assert getArguments() != null;
        presenter.setWorkshopTypeId(getArguments().getString(WORKSHOP_TYPE_ID));
        presenter.initialize(this);
        adapter = new WorkshopAdapter();

        binder.list.setHasFixedSize(true);
        binder.list.setNestedScrollingEnabled(false);
        binder.list.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener<WorkshopViewModel>() {
            @Override
            public void onItemClick(int adapterPosition, WorkshopViewModel item) {
                presenter.onItemClicked(item);
            }
        });
        setupSpinner();
    }

    private void setupSpinner() {
        spinnerAdapter = new DisciplineSpinnerAdapter(getActivity(), new ArrayList<DisciplineViewModel>());
        spinnerAdapter.add(new DisciplineViewModel(getString(R.string.all)));
        binder.spinnerDisciplines.setAdapter(spinnerAdapter);
        binder.spinnerDisciplines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.onDisciplineClicked(spinnerAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return Injection.providePlaceholderHelper(binder.content);
    }

    @Override
    public void setupList(List<WorkshopViewModel> items) {
        hideProgress();
        adapter.setList(items);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void openLessons(WorkshopViewModel item) {
        Bundle extras = new Bundle();
        extras.putString(LessonsActivity.WORKSHOP_ID, item.id);
        extras.putString(LessonsActivity.WORKSHOP_TITLE, item.title);
        startActivity(LessonsActivity.class, extras);
    }

    @Override
    public void setupDisciplines(List<DisciplineViewModel> result) {
        spinnerAdapter.addAll(result);
    }

    @Override
    public void showNoResultsPlaceholder() {
        showPlaceholder(R.drawable.ic_calendar_page_empty, R.string.no_workshops_found_description, null);
    }

    @Override
    public void openDetails(WorkshopViewModel item) {
        Bundle extras = new Bundle();
        extras.putString(WorkshopDetailActivity.WORKSHOP_ID, item.id);
        startActivity(WorkshopDetailActivity.class, extras);
    }

    public static Fragment newInstance(String workshopTypeId) {
        Fragment fragment = new WorkshopListFragment();
        Bundle args = new Bundle();
        args.putString(WORKSHOP_TYPE_ID, workshopTypeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRetryLoadingClicked() {
        presenter.onRetryClicked(spinnerAdapter.getCount() == 1);
    }
}