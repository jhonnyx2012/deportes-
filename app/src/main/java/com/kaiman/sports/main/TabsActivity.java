package com.kaiman.sports.main;

import com.core.presentation.activity.BaseFragmentActivity;
import com.core.util.TabNavigationController;
import com.kaiman.sports.R;
import com.kaiman.sports.data.remote.push.SportsFirebaseInstanceIdService;
import com.kaiman.sports.databinding.ActivityTabsBinding;
import com.kaiman.sports.main.news.NewsFragment;
import com.kaiman.sports.main.profile.ProfileFragment;
import com.kaiman.sports.main.workshops.WorkshopsFragment;
import com.kaiman.sports.utils.BottomNavigationViewHelper;
import com.kaiman.sports.utils.Utils;

public class TabsActivity extends BaseFragmentActivity<ActivityTabsBinding> {
    private TabNavigationController navController;

    @Override
    protected void initView() {
        Utils.checkAppVersion(this);
        SportsFirebaseInstanceIdService.sendRegistrationToServer();
        BottomNavigationViewHelper.disableShiftMode(binder.navigation);
        navController = new TabNavigationController(binder.navigation, new TabNavigationController.Finder() {
            @Override
            public int getPositionById(int itemId) {
                switch (itemId) {
                    case R.id.navigation_news:return 0;
                    case R.id.navigation_workshops:return 1;
                    //case R.id.navigation_activities:return 2;
                    case R.id.navigation_profile: return 2;
                }
                return TabNavigationController.INVALID_INDEX;
            }
        });
        navController.setFragments(
                this,
                new NewsFragment(),
                new WorkshopsFragment(),
                //new ActivitiesFragment(),
                new ProfileFragment());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tabs;
    }

    @Override
    public void onBackPressed() {
        if (navController.allowBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.container;
    }
}
