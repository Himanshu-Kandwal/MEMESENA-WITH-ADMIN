package msm.MemeSena.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import msm.MemeSena.Adapter.CustomViewPagerAdapter;
import msm.MemeSena.Base.BaseFragment;
import msm.MemeSena.Model.TabDetailsModel;
import msm.MemeSena.R;

public class FragServerStickersListAndSavedStickersList extends BaseFragment implements SearchView.OnQueryTextListener {
    ViewPager mViewPager;
    TabLayout tabLayout;
    CustomViewPagerAdapter adapter;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_serverstickerslist_and_savedstickerslist;
    }

    @Override
    protected void initViewsHere() {
        setHasOptionsMenu(true);
        mViewPager = (ViewPager) getView().findViewById(R.id.mViewPager);
        tabLayout = (TabLayout) getView().findViewById(R.id.tabsLayout);
        setupViewPagerWithTabLayout();
    }

    private void setupViewPagerWithTabLayout() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(getString(R.string.txt_isLoadDefaultPackList), true);
        Fragment fragment = new FragStickerMakerList();
        fragment.setArguments(bundle);

        adapter = new CustomViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TabDetailsModel(getString(R.string.tab_labal_explore), new FragServerStickerList()));
        adapter.addFragment(new TabDetailsModel(getString(R.string.tab_labal_saved), fragment));
        //adapter.addFragment(new TabDetailsModel(getString(R.string.tab_labal_saved), new FragSavedStickersList()));

        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                searchView.onActionViewCollapsed();
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    SearchView searchView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sticker_list_activity, menu);
        MenuItem searchItem = menu.findItem(R.id.ic_menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {
        if (mViewPager.getCurrentItem() == 0)
            ((FragServerStickerList) adapter.getItem(0)).SerchingQueryText(searchText);
        else if (mViewPager.getCurrentItem() == 1)
            ((FragStickerMakerList) adapter.getItem(1)).SerchingQueryText(searchText);
        return false;
    }
}