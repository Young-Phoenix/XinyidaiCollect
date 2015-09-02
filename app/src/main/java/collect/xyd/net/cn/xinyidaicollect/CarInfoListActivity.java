package collect.xyd.net.cn.xinyidaicollect;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.viewpagerindicator.TabPageIndicator;

import collect.xyd.net.cn.xinyidaicollect.adapter.TabAdapter;

/**
 * Created by Administrator on 2015/8/7 0007.
 */
public class CarInfoListActivity extends BaseActivity {
    private TabPageIndicator mIndicator ;
    private ViewPager mViewPager ;
    private FragmentPagerAdapter mAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_info_list_layout);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.car_info);

        mIndicator = (TabPageIndicator) findViewById(R.id.id_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_pager);
        mAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            /*case R.id.action_add:
                if (app.user != null) {
                    Intent intent = new Intent(this, CollectActivity.class);
                    intent.putExtra("type", CollectActivity.CAR_TYPE);
                    startActivity(intent);
                }else {
                    T.showShort(this, "请先登录");
                    startActivityForResult(new Intent(this, LoginActivity.class), MainActivity.LOGIN_REQUEST);
                }
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }
}
