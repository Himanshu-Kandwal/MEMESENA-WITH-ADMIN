package msm.MemeSena.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import msm.MemeSena.Model.TabDetailsModel;

public class CustomViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<TabDetailsModel> tabDetailsArrayList = new ArrayList<>();

    public CustomViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return tabDetailsArrayList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return tabDetailsArrayList.size();
    }

    public void addFragment(TabDetailsModel tabDetail) {
        tabDetailsArrayList.add(tabDetail);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabDetailsArrayList.get(position).getTabName();
    }
}