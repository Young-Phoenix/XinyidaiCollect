package collect.xyd.net.cn.xinyidaicollect.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import collect.xyd.net.cn.xinyidaicollect.fragment.CarInfoListFragment;
import collect.xyd.net.cn.xinyidaicollect.fragment.HouseInfoListFragment;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;

/**
 * Created by Administrator on 2015/8/7 0007.
 */
public class HouseTabAdapter extends FragmentPagerAdapter {
    public static final String[] TITLES = new String[]{"未审核", "未通过", "已通过"};

    public HouseTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        HouseInfoListFragment fragment = new HouseInfoListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TYPE, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position % TITLES.length];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }
}
