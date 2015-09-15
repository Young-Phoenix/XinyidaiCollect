package collect.xyd.net.cn.xinyidaicollect.fragment;

import java.util.Collections;
import java.util.List;

import collect.xyd.net.cn.xinyidaicollect.MainActivity;
import collect.xyd.net.cn.xinyidaicollect.adapter.BusinessAdapter;
import collect.xyd.net.cn.xinyidaicollect.service.BusinessInfo;
import collect.xyd.net.cn.xinyidaicollect.utils.BusinessInfoComparator;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/8/6 0006.
 */
public class HouseBusinessListFragment extends BusinessListFragment {

    @Override
    public void onStart() {
        super.onStart();

        initData();
    }
    public void setData(List<BusinessInfo> businesses){
        this.businesses = businesses;
        Collections.sort(businesses, new BusinessInfoComparator());
        ((BusinessAdapter)adapter).setBusinesses(businesses);
        adapter.notifyDataSetChanged();
    }
    private void initData() {
        if(setBusinessDataListener==null) {
            setBusinessDataListener = (SetBusinessDataListener) getActivity().getSupportFragmentManager().findFragmentByTag(MainActivity.MAP_TAG);
        }
        setBusinessDataListener.setDataByTag("house");
        /*for (int i = 0; i < 10; i++) {
            businesses.add(new BusinessInfo("北京新易贷投资顾问有限公司","河南省新乡市华兰大道509号创业园三号园南楼一楼西厅","400-886-7860","200"));
        }*/
        /*dialog = DialogUtil.createLoadingDialog(getActivity(), "加载中……");
        dialog.show();
        Map params = new HashMap();
        params.put(Constants.TOKEN, ((App) getActivity().getApplication()).user.getToken());
        params.put(Constants.TYPE, type + "");
        new HttpRequestUtil((BaseActivity) getActivity()).stringRequest(Constants.GET_CAR_INFO, RequestUri.GET_CAR_BIZ, params);*/
    }

    @Override
    public void onResult(List<BusinessInfo> businesses) {
        if(businesses.size()>0) {
            this.businesses.clear();
            for (BusinessInfo businessInfo : businesses) {
                if (businessInfo.getType() == MapFragment.HOUSE) {
                    this.businesses.add(businessInfo);
                }
            }
            ((BusinessAdapter)adapter).setBusinesses(this.businesses);
            adapter.notifyDataSetChanged();
        }else{
            T.showShort(getActivity(),"无更新内容");
        }
    }
}
