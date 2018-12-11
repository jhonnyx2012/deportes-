package com.core.presentation.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.core.R;

import java.util.List;

/**
 * Created by Jhonny Barrios on 23/08/2017.
 *
 * The activity only will execute operations that affect the UI. These operations
 * are triggered by its presenter.
 *
 */
public abstract class BaseActivity<BINDER extends ViewDataBinding> extends AppCompatActivity {
    protected Toolbar mToolbar;
    protected BINDER binder;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected View.OnClickListener onBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getMenuId(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected int getMenuId() {return R.menu.empty_menu;}

    /**
     * The onCreate base will init this view
     */
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        setEnterTransitionAnimations();
        super.onCreate(savedInstanceState);
        binder=DataBindingUtil.setContentView(this,getLayoutId());
        injectDependencies();
        setupToolbar();
        initView();
    }

    protected abstract void initView();

    /**
     * Setup the object graph and inject the dependencies needed on this activity.
     */
    protected void injectDependencies(){}

    protected abstract @LayoutRes int getLayoutId();

    @Override protected void onDestroy() {
        unbindViews();
        super.onDestroy();
    }

    protected void setupToolbar() {setupToolbar(R.id.toolbar);}

    protected void setupToolbar(int toolbarId) {
        mToolbar = findViewById(toolbarId);
        try {
            setSupportActionBar(mToolbar);
        }catch(Exception e){e.printStackTrace();}
    }

    protected void startActivity(Class activityClass) {
        Intent i = new Intent(this, activityClass);
        startActivity(i);
    }


    public void startActivity(Class activityClass, Bundle extras) {
        Intent i = new Intent(this, activityClass);
        if (extras != null) {
            i.putExtras(extras);
        }
        startActivity(i);
    }

    private void unbindViews() {
        if(binder!=null)
            binder.unbind();
    }

    protected void setEnterTransitionAnimations() {
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected void setExitTransitionAnimations() {
        //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
        setExitTransitionAnimations();
    }

    protected List<Fragment> getFragmentStack(){return getSupportFragmentManager().getFragments();}

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    protected void showUpButton(boolean show) {
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
    }

    protected void hideActionBar() {
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
    }
}