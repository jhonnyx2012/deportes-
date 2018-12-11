package com.kaiman.sports.main.workshops;

import android.support.v4.view.ViewPager;
import com.core.presentation.adapter.FastFragmentPagerAdapter;
import com.core.presentation.fragment.BaseFragment;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.FragmentWorkshopsBinding;
import com.kaiman.sports.main.workshops.view.MyWorkshopsFragment;
import com.kaiman.sports.main.workshops.view.WorkshopListFragment;

public class WorkshopsFragment extends BaseFragment<FragmentWorkshopsBinding> {
   private static final String TYPE_RECREATIVES_ID = "1";
   private static final String TYPE_FORMATIVES_ID = "3";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_workshops;
    }

    @Override
    protected void initView() {
        final FastFragmentPagerAdapter adapter = new FastFragmentPagerAdapter(getChildFragmentManager());
        adapter.add(new MyWorkshopsFragment(), getString(R.string.my_workshops));
        adapter.add(WorkshopListFragment.newInstance(TYPE_FORMATIVES_ID), getString(R.string.formatives));
        adapter.add(WorkshopListFragment.newInstance(TYPE_RECREATIVES_ID), getString(R.string.recreatives));
        binder.pager.setOffscreenPageLimit(3);
        binder.pager.setAdapter(adapter);
        binder.tabs.setupWithViewPager(binder.pager);

        binder.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.getItem(position).onResume();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
