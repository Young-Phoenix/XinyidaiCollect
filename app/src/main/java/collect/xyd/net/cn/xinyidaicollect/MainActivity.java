package collect.xyd.net.cn.xinyidaicollect;

import android.app.ActionBar;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.net.xyd.http.HttpRequestUtil;
import cn.net.xyd.http.RequestUri;
import collect.xyd.net.cn.xinyidaicollect.entity.BusinessInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.CarInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.Coor;
import collect.xyd.net.cn.xinyidaicollect.entity.RequestResult;
import collect.xyd.net.cn.xinyidaicollect.entity.User;
import collect.xyd.net.cn.xinyidaicollect.fragment.MapFragment;
import collect.xyd.net.cn.xinyidaicollect.json.JsonUtil;
import collect.xyd.net.cn.xinyidaicollect.receiver.NetWorkReceiver;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.CoorConvert;
import collect.xyd.net.cn.xinyidaicollect.utils.DialogUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.L;
import collect.xyd.net.cn.xinyidaicollect.utils.SPUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

public class MainActivity extends BaseActivity implements HttpRequestUtil.HttpRequestListener, MapFragment.MyLocationListener {
    public static int LOGIN_REQUEST = 1;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout relativelayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    public static final String MAP_TAG = "map";
    @Bind(R.id.menu_login)
    TextView menu_login;
   /* @Bind(R.id.menu_home)
    private TextView menu_home;
    @Bind(R.id.menu_near)
    private TextView menu_near;
    @Bind(R.id.menu_collect)
    private TextView menu_collect;
    @Bind(R.id.menu_add)
    private TextView menu_add;
    @Bind(R.id.menu_logout)
    private TextView menu_logout;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initConfig();
        initView();
        initFragment();
        initData();
        registerDateTransReceiver();
    }

    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private NetWorkReceiver netWorkReceiver;

    private void registerDateTransReceiver() {
        netWorkReceiver = new NetWorkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_CHANGE_ACTION);
        filter.setPriority(1000);
        registerReceiver(netWorkReceiver, filter);
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
    }

    /**
     * 初始化View
     */
    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        relativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        //MapFragment map = new MapFragment();
        Fragment map = MapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, map, MAP_TAG).addToBackStack(null).commit();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //setTitle(R.string.app_home);
        if (app.user != null) {
            menu_login.setText(app.user.getUsername());
        } else {
            menu_login.setText(R.string.menu_login);
        }

    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showOrHiddenNav();
                break;
            case R.id.action_map:
                showOrHiddenMap();
            case R.id.action_location:
                startLocation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private MapFragment mapFragment;
    //显示或隐藏地图
    private void showOrHiddenMap(){
        if(mapFragment==null) {
            mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MAP_TAG);
        }
        if(!mapFragment.showOrHiddenMap()){
            T.showShort(this,"地图尚未加载完成");
        }
    }
    //定位
    private void startLocation(){
        if(mapFragment==null) {
            mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MAP_TAG);
        }
        mapFragment.startLocation();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case Constants.MAP_FRAGMENT:
                setTitle(R.string.app_home);
                break;
        }
    }

    //如果导航是显示状态则隐藏，如果是隐藏则显示
    private void showOrHiddenNav() {
        if (mDrawerLayout.isDrawerOpen(relativelayout)) {
            mDrawerLayout.closeDrawer(relativelayout);
            getActionBar().setTitle(R.string.app_home);
        } else {
            mDrawerLayout.openDrawer(relativelayout);
            getActionBar().setTitle(R.string.app_name);
        }
    }

    //导航菜单按钮监听方法
    public void onNavMenuClick(View view) {
        showOrHiddenNav();
        switch (view.getId()) {
            case R.id.menu_login:
                if (app.user == null) {
                    startLoginActivity(false,null);
                }
                break;
            case R.id.menu_house:
                startActivity(true,HouseInfoListActivity.class,null);
                /*if (app.user != null) {
                    Intent intent = new Intent(this, HouseInfoListActivity.class);
                    startActivity(intent);
                } else {
                    T.showShort(this, "请先登录");
                    startActivityForResult(new Intent(this, LoginActivity.class), MainActivity.LOGIN_REQUEST);
                }*/
               /* if (app.user != null) {
                    Intent intent = new Intent(this, CollectActivity.class);
                    intent.putExtra("type", CollectActivity.HOUSE_TYPE);
                    startActivity(intent);
                } else {
                    T.showShort(this, "请先登录");
                    startActivityForResult(new Intent(this, LoginActivity.class), MainActivity.LOGIN_REQUEST);
                }*/
                break;
            case R.id.menu_car:
                startActivity(true, CarInfoListActivity.class,null);
                /*if (app.user != null) {
                    Intent intent = new Intent(this, CarInfoListActivity.class);
                    startActivity(intent);
                } else {
                    T.showShort(this, "请先登录");
                    startActivityForResult(new Intent(this, LoginActivity.class), MainActivity.LOGIN_REQUEST);
                }*/
                break;
            case R.id.menu_add_shop:
                Bundle bundle = new Bundle();
                bundle.putInt("type", CollectActivity.SHOP_INFO_TYPE);
                startActivity(true,CollectActivity.class,bundle);
                /*if (app.user != null) {
                    Intent intent = new Intent(this, CollectActivity.class);
                    intent.putExtra("type", CollectActivity.SHOP_INFO_TYPE);
                    startActivity(intent);
                } else {
                    T.showShort(this, "请先登录");
                    startActivityForResult(new Intent(this, LoginActivity.class), MainActivity.LOGIN_REQUEST);
                }*/

                break;
            case R.id.menu_logout:
                clearUserInfo();
                this.finish();
                System.exit(0);
                break;
        }
    }

    private void clearUserInfo() {
        SPUtils.remove(this.getApplicationContext(), Constants.USERNAME);
        SPUtils.remove(this.getApplicationContext(), Constants.TOKEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == LOGIN_REQUEST) {
            Bundle bundle = data.getExtras();
            String username = bundle.getString(Constants.USERNAME);
            String token = bundle.getString(Constants.TOKEN);
            SPUtils.put(this.getApplicationContext(), Constants.TOKEN, token);
            SPUtils.put(this.getApplicationContext(), Constants.USERNAME, username);
            app.user = new User(username, token);
            menu_login.setText(username);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private long temp = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - temp > 2000) {
            T.showShort(this, R.string.press_again_exit);
            temp = System.currentTimeMillis();
        } else {
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (netWorkReceiver != null) {
            unregisterReceiver(netWorkReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(app.user!=null && "登录".equals(menu_login.getText())){
            menu_login.setText(app.user.getUsername());
        }
    }

    @Override
    public void onResponse(int requestCode, String data) {
        dismissDialog();
        try {
            String resultData = replaceSpecialtyStr(new String(data.getBytes("ISO-8859-1"), "GBK"),null,"");
            if (requestCode == Constants.GET_CAR_BUSINESS) {
                JsonUtil<List<BusinessInfo>> jsonUtil = new JsonUtil<List<BusinessInfo>>();
                RequestResult<List<BusinessInfo>> requestResult = jsonUtil.json2Obj(resultData, new TypeReference<RequestResult<List<BusinessInfo>>>() {
                });
                if (requestResult != null) {
                    switch (requestResult.getMsg().getResultCode()) {
                        case 200:
                            List<BusinessInfo> businessInfoList = requestResult.getData();
                            if(mapFragment==null) {
                                mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MAP_TAG);
                            }
                            mapFragment.addMarkersToMap(businessInfoList);
                            mapFragment.addBusinessInfo(businessInfoList);
                            break;
                        case 400:
                            T.showShort(this, new String(requestResult.getMsg().getMessage()));
                            break;
                    }
                }
            }
            L.d(resultData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onErrorResponse(int requestCode, String error) {
        dismissDialog();
        T.showShort(this, error);
        L.d(error);
    }
    /**
     * @param geoLat 纬度
     * @param geoLng 经度
     */
    @Override
    public void success(double geoLat, double geoLng) {
        showDialog(this,"加载中……");
        Coor bdCoor = CoorConvert.bd_encrypt(geoLat, geoLng);

        if (app.user != null) {
            //本地存放百度坐标系
            app.user.setGeoLat(bdCoor.getLat());
            app.user.setGeoLng(bdCoor.getLng());
        }

        Map params = new HashMap();
        params.put("gps_x", bdCoor.getLat() + "");
        params.put("gps_y", bdCoor.getLng() + "");
        //gps_x 纬度 gps_y 经度
        /*params.put("gps_y", 113.917276 + "");
        params.put("gps_x", 35.287356 + "");*/

        new HttpRequestUtil(this).stringRequest(Constants.GET_CAR_BUSINESS, RequestUri.GET_CAR_BIZ, params);
    }
}
