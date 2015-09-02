package collect.xyd.net.cn.xinyidaicollect.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import collect.xyd.net.cn.xinyidaicollect.App;
import collect.xyd.net.cn.xinyidaicollect.CollectActivity;
import collect.xyd.net.cn.xinyidaicollect.LoginActivity;
import collect.xyd.net.cn.xinyidaicollect.MainActivity;
import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.entity.BusinessInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.Coor;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.CoorConvert;
import collect.xyd.net.cn.xinyidaicollect.utils.L;
import collect.xyd.net.cn.xinyidaicollect.utils.SPUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.T;


/**
 * Created by Administrator on 2015/7/15 0015.
 */
public class MapFragment extends Fragment implements LocationSource,
        AMapLocationListener, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMapLoadedListener, BusinessListFragment.SetBusinessDataListener {
    private static final long TIME = 300 * 1000;
    public static final int HOUSE = 1;
    public static final int CAR = 2;
    private static final String TAG = "MapLocation";
    private static MapFragment mapFragment = null;
    public static final int POSITION = 0;
    private MapView mapView;
    private AMap aMap;
    private View mapLayout;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mLocationManagerProxy;
    private FragmentTabHost mTabHost;
    private CarBusinessListFragment carBusinessListFragment;
    private HouseBusinessListFragment houseBusinessListFragment;
    private TextView loading_location;

    @Override
    public void setDataByTag(String tabTag) {
        setTabData(tabTag);
    }

    public void startLocation() {
        if (mLocationManagerProxy != null) {
            mLocationManagerProxy.removeUpdates(this);
            mLocationManagerProxy.requestLocationData(
                    LocationProviderProxy.AMapNetwork, TIME, 10, this);
            if (loading_location.getVisibility() == View.GONE) {
                loading_location.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 自定义定位成功的监听，通知主activity去后台获取经销商信息
     */
    public interface MyLocationListener {
        void success(double geoLat, double geoLng);
    }

    private MyLocationListener locationListener;

    public static Fragment newInstance() {
        if (mapFragment == null) {
            synchronized (MapFragment.class) {
                if (mapFragment == null) {
                    mapFragment = new MapFragment();
                }
            }
        }
        return mapFragment;
    }

    // 定义数组来存放Fragment界面
    private Class fragmentArray[] = {CarBusinessListFragment.class, HouseBusinessListFragment.class};

    // 定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_car_business_btn,
            R.drawable.tab_house_business_btn};
    private String mTextviewArray[];
    private String mTabTag[];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mapLayout == null) {
            mapLayout = inflater.inflate(R.layout.map_fragment, null);
            loading_location = (TextView) mapLayout.findViewById(R.id.loading_location);
            mTabHost = (FragmentTabHost) mapLayout.findViewById(android.R.id.tabhost);
            mapView = (MapView) mapLayout.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            initData(inflater);
        } else {
            if (mapLayout.getParent() != null) {
                ((ViewGroup) mapLayout.getParent()).removeView(mapLayout);
            }
        }
        return mapLayout;
    }

    private void initData(LayoutInflater inflater) {
        mTabHost.setup(getActivity(), getActivity().getSupportFragmentManager(), R.id.fl_fragmentContent);
        mTextviewArray = getResources().getStringArray(R.array.tab_menu);
        mTabTag = getResources().getStringArray(R.array.tab_tag);
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTabTag[i]).setIndicator(
                    getTabItemView(inflater, i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            // mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.home_btn_bg);
        }
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 初始化tab选项卡视图
     *
     * @param inflater
     * @param index
     * @return
     */
    private View getTabItemView(LayoutInflater inflater, int index) {
        View view = inflater.inflate(R.layout.tab_item_view, null);
        /*
         * ImageView imageView = (ImageView) view.findViewById(R.id.iamge_view);
		 * imageView.setImageResource(mImageViewArray[index]);
		 */
        TextView textView = (TextView) view.findViewById(R.id.tv_tab);
        textView.setText(mTextviewArray[index]);
        Drawable drawable = getResources().getDrawable(mImageViewArray[index]);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
        return view;
    }

    /**
     * 显示或隐藏地图
     */
    public boolean showOrHiddenMap() {
        if (mapView != null) {
            if (mapView.getVisibility() == View.GONE) {
                mapView.setVisibility(View.VISIBLE);
                mapView.startAnimation(getShowAnimation());
            } else {
                mapView.setVisibility(View.GONE);
                mapView.startAnimation(getHiddenAnimation());
            }
            return true;
        }
        return false;
    }

    private Animation mShowAction;
    private Animation mHiddenAction;

    private Animation getShowAnimation() {
        if (mShowAction == null) {
            mShowAction = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_enter);
        }
        return mShowAction;
    }

    private Animation getHiddenAnimation() {
        if (mHiddenAction == null) {
            mHiddenAction = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_exit);
        }
        return mHiddenAction;
    }

    protected LayoutAnimationController getAnimationController() {
        LayoutAnimationController controller;
        Animation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);// 从0.5倍放大到1倍
        anim.setDuration(1500);
        controller = new LayoutAnimationController(anim, 0.1f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        //addMarkersToMap();// 往地图上添加marker
    }

    /**
     * 在地图上添加marker
     */
    public void addMarkersToMap(List<BusinessInfo> businessInfos) {
        ArrayList<MarkerOptions> markerOpts = new ArrayList<MarkerOptions>();
        for (BusinessInfo businessInfo : businessInfos) {
            Coor ggCoor = CoorConvert.bd_decrypt(businessInfo.getGps_x(), businessInfo.getGps_y());
            MarkerOptions markerOptions = new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .position(new LatLng(ggCoor.getLat(), ggCoor.getLng()))
                    .title(businessInfo.getAgent_name())
                    .snippet(businessInfo.getAddress())
                    .draggable(false);
            if (businessInfo.getType() == 2) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            markerOpts.add(markerOptions);
            Marker marker = aMap.addMarker(markerOptions);
            marker.setObject(businessInfo);
        }
    }

    /**
     * 为carbusinesslistfragment和HouseBusinessListFragment填充数据
     */
    public void addBusinessInfo(List<BusinessInfo> businessInfos) {
        setCarBusiAndHouseBusiData(businessInfos);
        setTabData(mTabHost.getCurrentTabTag());
    }

    private List houseBusies = new LinkedList();
    private List carBusies = new LinkedList();

    /**
     * 分别设置车商和中介信息
     *
     * @param businessInfos
     */
    public void setCarBusiAndHouseBusiData(List<BusinessInfo> businessInfos) {
        houseBusies.clear();
        carBusies.clear();
        for (BusinessInfo businessInfo : businessInfos) {
            switch (businessInfo.getType()) {
                case HOUSE:
                    houseBusies.add(businessInfo);
                    break;
                case CAR:
                    carBusies.add(businessInfo);
                    break;
            }
        }

    }

    /**
     * 根据tabtag设置当前tab的数据
     *
     * @param tabTag
     */
    private void setTabData(String tabTag) {
        if (tabTag.equalsIgnoreCase("car")) {
            if (carBusinessListFragment == null) {
                carBusinessListFragment = (CarBusinessListFragment) getActivity().getSupportFragmentManager().findFragmentByTag("car");
            }
            carBusinessListFragment.setData(carBusies);
        } else if (tabTag.equalsIgnoreCase("house")) {
            if (houseBusinessListFragment == null) {
                houseBusinessListFragment = (HouseBusinessListFragment) getActivity().getSupportFragmentManager().findFragmentByTag("house");
            }
            houseBusinessListFragment.setData(houseBusies);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(Constants.MAP_FRAGMENT);
        try {
            locationListener = (MyLocationListener) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + "must implements CommitListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 激活定位
     *
     * @param listener
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        deactivate();
        mListener = listener;
        if (mLocationManagerProxy == null) {
            mLocationManagerProxy = LocationManagerProxy.getInstance(getActivity());
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用destroy()方法
            // 其中如果间隔时间为-1，则定位只定一次
            /*
             * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
        }
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, TIME, 10, this);
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationManagerProxy != null) {
            mLocationManagerProxy.removeUpdates(this);
            mLocationManagerProxy.destroy();
        }
        mLocationManagerProxy = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            int errorCode = aMapLocation.getAMapException().getErrorCode();
            if (errorCode == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                // 获取位置信息
                Double geoLat = aMapLocation.getLatitude();
                Double geoLng = aMapLocation.getLongitude();
                //获取地址信息
                String province = aMapLocation.getProvince();
                String city = aMapLocation.getCity();
                String district = aMapLocation.getDistrict();
                String address = aMapLocation.getAddress();

                SPUtils.put(getActivity(), Constants.KEY_LAT, geoLat);
                SPUtils.put(getActivity(), Constants.KEY_LNG, geoLng);
                SPUtils.put(getActivity(),Constants.KEY_PROVINCE,province);
                SPUtils.put(getActivity(),Constants.KEY_CITY,city);
                SPUtils.put(getActivity(),Constants.KEY_DISTRICT,district);
                SPUtils.put(getActivity(),Constants.KEY_ADDRESS,address);

                L.d(TAG, "纬度：" + geoLat + ";" + "经度：" + geoLng);
                L.d(TAG, "定位方式：" + aMapLocation.getProvider());
                locationListener.success(geoLat, geoLng);
                if (loading_location.getVisibility() == View.VISIBLE) {
                    loading_location.setVisibility(View.GONE);
                }
                //T.showShort(getActivity(),R.string.location_success);
            } else {
                //T.showLong(getActivity(),aMapLocation.getAMapException().getErrorMessage());
            }
           /* switch (errorCode) {
                case 0:
                    mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                    // 获取位置信息
                    Double geoLat = aMapLocation.getLatitude();
                    Double geoLng = aMapLocation.getLongitude();
                    L.d(TAG, "纬度：" + geoLat + "\n" + "经度：" + geoLng);
                    L.d(TAG, "定位方式：" + aMapLocation.getProvider());
                    break;
                case 21:
                    T.showShort(getActivity(),R.string.io_exception);
                    break;
                case 22:
                    T.showShort(getActivity(),R.string.connect_exception);
                    break;
                case 23:
                    T.showShort(getActivity(),R.string.timeout_exception);
                    break;
                case 24:
                    T.showShort(getActivity(),R.string.invalid_params_exception);
                    break;
                case 25:
                    T.showShort(getActivity(),R.string.null_point_exception);
                    break;
                case 26:
                    T.showShort(getActivity(),R.string.url_exception);
                    break;
                case 27:
                    T.showShort(getActivity(),R.string.unknown_host_exception);
                    break;
                case 28:
                    T.showShort(getActivity(),R.string.connect_server_exception);
                    break;
                case 29:
                    T.showShort(getActivity(),R.string.protocol_parse_exception);
                    break;
                case 30:
                    T.showShort(getActivity(),R.string.http_connect_exception);
                    break;
                case 31:
                    T.showShort(getActivity(),R.string.unknown_exception);
                    break;
                case 32:
                    T.showShort(getActivity(),R.string.key_exception);
                    break;

            }*/
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }
        return true;
    }

    /**
     * 监听点击infowindow窗口事件回调
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        //此处处理信息采集事件
        if (((App) getActivity().getApplication()).user != null) {
            Intent intent = new Intent(getActivity(), CollectActivity.class);
            intent.putExtra("type", ((BusinessInfo) marker.getObject()).getType());
            intent.putExtra("agent_id", ((BusinessInfo) marker.getObject()).getAgent_id());
            startActivity(intent);
        } else {
            T.showShort(getActivity(), "请先登录");
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), MainActivity.LOGIN_REQUEST);
        }

    }

    /**
     * 监听amap地图加载成功事件回调
     */
    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(Constants.XINXIANG1).include(Constants.XINXIANG2)
                .build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
    }
}
