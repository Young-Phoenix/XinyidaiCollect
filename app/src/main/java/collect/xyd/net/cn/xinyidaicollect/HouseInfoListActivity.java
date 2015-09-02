package collect.xyd.net.cn.xinyidaicollect;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.viewpagerindicator.TabPageIndicator;

import collect.xyd.net.cn.xinyidaicollect.adapter.HouseTabAdapter;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class HouseInfoListActivity extends BaseActivity{
    private TabPageIndicator mIndicator ;
    private ViewPager mViewPager ;
    private FragmentPagerAdapter mAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_info_list_layout);
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
        mIndicator = (TabPageIndicator) findViewById(R.id.id_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_pager);
        mAdapter = new HouseTabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager, 0);
    }
}
