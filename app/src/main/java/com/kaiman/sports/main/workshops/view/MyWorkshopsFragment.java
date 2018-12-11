package com.kaiman.sports.main.workshops.view;

import android.os.Bundle;
import android.view.View;

import com.core.presentation.adapter.OnItemClickListener;
import com.core.presentation.contract.BaseView;
import com.core.presentation.fragment.BaseRequestFragment;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.FragmentMyWorkshopsBinding;
import com.kaiman.sports.main.workshops.adapter.MyWorkshopAdapter;
import com.kaiman.sports.main.workshops.contract.MyWorkshops;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;

import java.util.List;

public class MyWorkshopsFragment extends BaseRequestFragment<FragmentMyWorkshopsBinding> implements MyWorkshops.View {

    MyWorkshops.Presenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_workshops;
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideMyWorkshopsPresenter();
    }

    @Override
    protected void initView() {
        presenter.initialize(this);
    }

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return Injection.providePlaceholderHelper(binder.content);
    }

    @Override
    public void setupList(List<WorkshopViewModel> items) {
        hideProgress();
        MyWorkshopAdapter adapter = new MyWorkshopAdapter();
        adapter.setList(items);
        binder.list.setAdapter(adapter);
        binder.list.setHasFixedSize(true);
        adapter.setOnItemClickListener(new OnItemClickListener<WorkshopViewModel>() {
            @Override
            public void onItemClick(int adapterPosition, WorkshopViewModel item) {
                Bundle extras = new Bundle();
                extras.putString(LessonsActivity.WORKSHOP_ID, item.id);
                extras.putString(LessonsActivity.WORKSHOP_TITLE, item.title);
                startActivity(LessonsActivity.class, extras);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.initialize(this);
    }

    @Override
    public void onRetryLoadingClicked() {
        presenter.onRetryClicked();
    }

    @Override
    public void showNoWorkshopsPlaceholder() {
        showPlaceholder(R.drawable.ic_sentiment_dissatisfied, R.string.no_my_workshops_placeholder, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRetryClicked();
            }
        });
    }
}