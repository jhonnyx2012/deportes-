package com.kaiman.sports.main.workshops.view;

import com.core.presentation.activity.BaseFragmentActivity;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ActivityFragmentContainerBinding;

/**
 * Created by jhonnybarrios on 3/20/18
 */

public class WorkshopListActivity extends BaseFragmentActivity<ActivityFragmentContainerBinding> {
    @Override
    protected int getFragmentContainerId() {
        return R.id.container;
    }

    @Override
    protected void initView() {
        showUpButton(true);
        //TODO, recibir el id de la disciplina para mostrarlos aca, aparte de en el titulo de la actividad
        setFragment(new WorkshopListFragment());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_container;
    }
}