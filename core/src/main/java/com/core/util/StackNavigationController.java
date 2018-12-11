package com.core.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.core.R;
import com.core.presentation.fragment.BaseFragment;
import java.util.List;

/**
 * Created by jhonnybarrios on 13-07-17.
 */

public class StackNavigationController {
    private final FragmentManager fragmentManager;
    private final int idContainer;

    public StackNavigationController(FragmentManager fragmentManager, int idContainer) {
        this.idContainer=idContainer;
        this.fragmentManager=fragmentManager;
    }

    public boolean allowBackPressed() {
        int childCount = fragmentManager.getBackStackEntryCount();
        if (childCount == 0) {
            return true;
        } else {
            List<Fragment> fragments = fragmentManager.getFragments();
            BaseFragment childFragment = (BaseFragment) fragments.get(fragments.size()-1);
            if (childFragment==null||childFragment.allowBackPressed()) {
                fragmentManager.popBackStackImmediate();
            }
            return false;
        }
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction trans = fragmentManager.beginTransaction();
        trans.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,R.anim.fade_in,R.anim.fade_out);
        trans.add(idContainer, fragment);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void clearBackStack() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
