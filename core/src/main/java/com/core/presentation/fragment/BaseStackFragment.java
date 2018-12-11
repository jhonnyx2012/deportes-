package com.core.presentation.fragment;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.core.util.StackNavigationController;

public abstract class BaseStackFragment<BINDER extends ViewDataBinding> extends BaseFragment<BINDER>{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initNavigation();
        return view;
    }

    public void initNavigation() {
        stackNavigationController = new StackNavigationController(getChildFragmentManager(), getNavigationContainer());
    }

    protected abstract int getNavigationContainer();

    public void addFragmentToStack(Fragment fragment) {
        BaseFragment base = getBaseStackFragment();
        if(base!=null)
            base.stackNavigationController.addFragment(fragment);
    }

    @Override public boolean isBaseStackFragment() {return true;}

    @Override public boolean allowBackPressed() {return stackNavigationController.allowBackPressed();}
}