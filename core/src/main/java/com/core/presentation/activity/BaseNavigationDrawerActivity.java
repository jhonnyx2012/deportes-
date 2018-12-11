package com.core.presentation.activity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.Window;

import com.core.R;
import com.core.presentation.fragment.BaseFragment;

/**
 * Created by carlosenock on 1/24/18.
 *
 * The activity only will execute operations that affect the UI. These operations
 * are triggered by its presenter.
 */

public abstract class BaseNavigationDrawerActivity<BINDER extends ViewDataBinding> extends BaseActivity<BINDER>
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Fragment topFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    protected void setFragment(Fragment fragment) {
        setFragment(fragment, false);
    }

    protected void setFragment(Fragment fragment, int idContainer) {
        setFragment(fragment, idContainer, false);
    }

    protected void setFragment(Fragment fragment, boolean addToBackStack) {
        setFragment(fragment, getFragmentContainerId(), addToBackStack);
    }

    protected abstract int getFragmentContainerId();

    protected void setFragment(Fragment fragment, int idContainer, boolean addToBackStack) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        if (addToBackStack)
            trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        trans.replace(idContainer, fragment);
        if (addToBackStack)
            trans.addToBackStack(fragment.getTag());
        trans.commit();
    }


    protected void addFragment(Fragment fragment, boolean show) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction()
                .add(getFragmentContainerId(), fragment);
        if (!show)
            trans.hide(fragment);
        else
            topFragment = fragment;
        trans.commit();
    }

    protected void hideFragment(Fragment fragment) {
        if (fragment == null) return;
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    protected void showFragment(Fragment fragmemt) {
        getSupportFragmentManager().beginTransaction().show(fragmemt).commit();
        if (topFragment != null && topFragment != fragmemt)
            hideFragment(topFragment);
        topFragment = fragmemt;
    }

    @Override public void onBackPressed() {
        BaseFragment actualFragment = getActualFragment();
        boolean canBack = actualFragment == null || actualFragment.allowBackPressed();
        try {
            if (canBack)
                super.onBackPressed();
        } catch (Exception ignored){}
    }

    protected BaseFragment getActualFragment(){
        return (BaseFragment) getSupportFragmentManager().findFragmentById(getFragmentContainerId());
    }

    protected Fragment getTopFragment() {
        return topFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
