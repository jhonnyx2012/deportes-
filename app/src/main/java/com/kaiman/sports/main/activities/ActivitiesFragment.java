package com.kaiman.sports.main.activities;

import com.core.presentation.contract.BaseView;
import com.core.presentation.fragment.BaseRequestFragment;
import com.core.util.PlaceholderRequestViewHelper;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.FragmentActivitiesBinding;
import com.kaiman.sports.main.activities.adapter.ActivityAdapter;
import java.util.List;

public class ActivitiesFragment extends BaseRequestFragment<FragmentActivitiesBinding> implements Activities.View {

    Activities.Presenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activities;
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideActivitiesPresenter();
    }

    @Override
    protected void initView() {
        presenter.initialize(this);
    }

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return new PlaceholderRequestViewHelper(binder.contentContainer);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setupList(List<ActivityViewModel> items) {
        hideProgress();
        ActivityAdapter adapter = new ActivityAdapter();
        adapter.setList(items);
        binder.list.setAdapter(adapter);
    }

    @Override
    public void onRetryLoadingClicked() {
        presenter.onRetryClicked();
    }
}
