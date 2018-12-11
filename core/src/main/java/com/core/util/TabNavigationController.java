package com.core.util;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;
import com.core.presentation.activity.BaseFragmentActivity;
import com.core.presentation.fragment.BaseFragment;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jhonnybarrios on 12/22/17
 */

public class TabNavigationController implements
        BottomNavigationView.OnNavigationItemReselectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    public static final int INVALID_INDEX = -1;
    private List<BaseFragment> fragments;
    private BaseFragmentActivity activity;
    private Finder positionFinder;

    public TabNavigationController(@NonNull BottomNavigationView navigation, @NonNull Finder positionFinder) {
        navigation.setOnNavigationItemReselectedListener(this);
        navigation.setOnNavigationItemSelectedListener(this);
        this.positionFinder = positionFinder;
    }

    public void setFragments(BaseFragmentActivity activity, BaseFragment... fragments) {
        this.activity=activity;
        this.fragments= Arrays.asList(fragments);
        for (BaseFragment fragment : fragments) {
            activity.addFragment(fragment,false);
        }
        navigateToTab(0);
    }

    private void navigateToTab(int position) {
        //Toast.makeText(activity, "NAVEGANDO A "+position, Toast.LENGTH_SHORT).show();
        AndroidUtils.hideSoftKeyboard(activity);
        activity.showFragment(getRootFragment(position));
    }

    private void resetTabBackStack(int newIndex) {
        //Toast.makeText(activity, "RESETTING BACKSTACK "+newIndex, Toast.LENGTH_SHORT).show();
        getRootFragment(newIndex).clearBackStack();
    }

    private BaseFragment getRootFragment(int index) {
        if(index<0) throw new IllegalStateException("Need to send an index that we know");
        return fragments.get(index);
    }

    public boolean allowBackPressed() {
        return ((BaseFragment)activity.getTopFragment()).allowBackPressed();
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        resetTabBackStack(positionFinder.getPositionById(item.getItemId()));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigateToTab(positionFinder.getPositionById(item.getItemId()));
        return true;
    }

    public interface Finder {
        int getPositionById(int itemId);
    }
}