package com.kaiman.sports.intro;

import com.core.presentation.activity.BaseSplashActivity;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.data.repository.user.UserDataSource;
import com.kaiman.sports.databinding.ActivitySplashBinding;
import com.kaiman.sports.main.TabsActivity;

/**
 * Created by jhonnybarrios on 3/11/18
 */

public class SplashActivity extends BaseSplashActivity<ActivitySplashBinding> {
    private UserDataSource userRepository;

    @Override
    protected void openNextActivity() {
        if (userRepository.getLoggedUser() != null) {
           startActivity(TabsActivity.class);
        } else {
            startActivity(StartActivity.class);
        }

        //startActivity(StartActivity.class);

    }

    @Override
    protected int getSplashTime() {
        return 500;
    }

    @Override
    protected void injectDependencies() {
        userRepository = Injection.provideUserRepository();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }
}
