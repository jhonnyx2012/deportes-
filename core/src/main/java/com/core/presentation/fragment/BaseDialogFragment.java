package com.core.presentation.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.core.R;
import com.core.presentation.activity.BaseActivity;

/**
 * Created by jhonnybarrios on 10/23/17
 */

public abstract class BaseDialogFragment<BINDER extends ViewDataBinding> extends DialogFragment {
    private Toolbar mToolbar;
    protected Context CONTEXT;
    protected BINDER binder;


    /**
     * Specify the layout of the fragment to be inflated in the [BaseFragment.onCreateView]
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        injectDependencies();
    }

    protected abstract void injectDependencies();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        setRetainInstance(true);
        return binder.getRoot();
    }

    protected abstract int getLayoutId();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = view.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            ((BaseActivity)getActivity()).setSupportActionBar(mToolbar);
        }
        initView();
    }

    protected abstract void initView();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context!=null)
            CONTEXT = context;
    }

    public void startActivity(Class activityClass) {
        if(getActivity()!=null){
            Intent i = new Intent(getActivity(), activityClass);
            startActivity(i);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(binder!=null)
            binder.unbind();
    }

    public void finishActivity() {
        if(getActivity()!=null)
            getActivity().finish();
    }


    protected void restartActivity() {
        ((BaseActivity)getActivity()).restartActivity();
    }

    public void popBackStack() {
        getActivity().onBackPressed();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if(dialog.getWindow()!=null)
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0.0f);
        }
    }
}