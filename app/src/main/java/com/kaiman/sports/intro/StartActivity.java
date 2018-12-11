package com.kaiman.sports.intro;

import android.view.View;

import com.core.presentation.activity.BaseActivity;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ActivityStartBinding;
import com.kaiman.sports.intro.login.LoginActivity;
import com.kaiman.sports.intro.register.RegisterActivity;

/**
 * Created by jhonnybarrios on 3/11/18
 */

public class StartActivity extends BaseActivity<ActivityStartBinding>{

    @Override
    protected void setExitTransitionAnimations() {
        overridePendingTransition(com.core.R.anim.fade_in, com.core.R.anim.fade_out);
    }

    @Override
    protected void initView() {
        binder.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(LoginActivity.class);
            }
        });

        binder.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity.class);
            }
        });

        binder.video.view.playVideo(R.raw.video_intro);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }
}
