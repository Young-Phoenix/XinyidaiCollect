package cn.net.xyd.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.net.xyd.area.CityModel;
import cn.net.xyd.area.DistrictModel;
import cn.net.xyd.area.ProvinceModel;
import cn.net.xyd.area.XmlParserHandler;
import collect.xyd.net.cn.xinyidaicollect.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * Created by Administrator on 2015/7/17 0017.
 */
public class AreaPopupWindow extends PopupWindow implements View.OnClickListener, OnWheelChangedListener{
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName ="";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode ="";
    /**
     * 解析省市区的XML数据
     */
    private Context mContext;
    protected void initProvinceDatas()
    {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = mContext.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i=0; i< provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j=0; j< cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k=0; k<districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    private OnAreaSelectListener listener;
    public AreaPopupWindow(Fragment fragment){
        this.mContext = fragment.getActivity();
        try {
            listener = (OnAreaSelectListener) fragment;
        }catch (Exception e){
            throw new ClassCastException(fragment.getClass()+"must implements interface OnAreaSelectListener");
        }
    }
    public AreaPopupWindow(Context context){
        this.mContext = context;
        try {
            listener = (OnAreaSelectListener) context;
        }catch (Exception e){
            throw new ClassCastException(context.getPackageName()+"must implements interface OnAreaSelectListener");
        }
    }

    public interface OnAreaSelectListener{
        void onSelect(String proviceName,String cityName,String districtName,String areaCode);
    }

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button mBtnConfirm;

    public void showArea(View view){
        if (popupWindow == null)
            initPopWindow();
        int[] arrayOfInt = new int[2];
        view.getLocationOnScreen(arrayOfInt);
        int x = arrayOfInt[0];
        int y = arrayOfInt[1];
        showPop(view, x, y + view.getHeight());
    }
    public void dismissArea(){
        if(popupWindow!=null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }
    /**
     * 使用popupwindow实现弹出菜单
     */
    private PopupWindow popupWindow;

    private void initPopWindow() {
        View popView = LayoutInflater.from(mContext).inflate(
                R.layout.area_layout, null);

        popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // 设置popwindow出现和消失动画
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        mViewProvince = (WheelView) popView.findViewById(R.id.id_province);
        mViewCity = (WheelView) popView.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) popView.findViewById(R.id.id_district);
        mBtnConfirm = (Button) popView.findViewById(R.id.btn_confirm);
        setUpListener();
        setUpData();
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    private void showPop(View parent, int x, int y) {
        // 设置popwindow显示位置
        popupWindow.showAtLocation(parent, 0, x, y);
        // 获取popwindow焦点
        popupWindow.setFocusable(true);
        // 设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();

    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(mContext, areas));
        mViewDistrict.setCurrentItem(0);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    @Override
    public void onClick(View v) {
        listener.onSelect(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName, mCurrentZipCode);
        dismissArea();
    }


}
