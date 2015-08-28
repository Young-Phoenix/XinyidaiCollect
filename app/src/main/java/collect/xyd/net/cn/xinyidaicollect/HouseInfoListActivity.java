package collect.xyd.net.cn.xinyidaicollect;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import collect.xyd.net.cn.xinyidaicollect.fragment.HouseInfoListFragment;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class HouseInfoListActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    FragmentPagerItemAdapter adapter;
    SmartTabLayout viewPagerTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_list_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.house_info);
        initData();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
           /* case R.id.action_add:
                if (app.user != null) {
                    Intent intent = new Intent(this, CollectActivity.class);
                    intent.putExtra("type", CollectActivity.HOUSE_TYPE);
                    startActivity(intent);
                }else {
                    T.showShort(this, "请先登录");
                    startActivityForResult(new Intent(this, LoginActivity.class), MainActivity.LOGIN_REQUEST);
                }
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.not_conformed, HouseInfoListFragment.class,new Bundler().putInt(Constants.TYPE, 0).get())
                .add(R.string.not_passed, HouseInfoListFragment.class, new Bundler().putInt(Constants.TYPE, 1).get())
                .add(R.string.conformed, HouseInfoListFragment.class,new Bundler().putInt(Constants.TYPE, 2).get())
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        viewPagerTab.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
