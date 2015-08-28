package collect.xyd.net.cn.xinyidaicollect.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.net.xyd.http.HttpRequestUtil;
import cn.net.xyd.http.RequestUri;
import collect.xyd.net.cn.xinyidaicollect.App;
import collect.xyd.net.cn.xinyidaicollect.BaseActivity;
import collect.xyd.net.cn.xinyidaicollect.CollectActivity;
import collect.xyd.net.cn.xinyidaicollect.LoginActivity;
import collect.xyd.net.cn.xinyidaicollect.MainActivity;
import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.adapter.BusinessAdapter;
import collect.xyd.net.cn.xinyidaicollect.adapter.CarInfoAdapter;
import collect.xyd.net.cn.xinyidaicollect.entity.BusinessInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.CarInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.Coor;
import collect.xyd.net.cn.xinyidaicollect.entity.RequestResult;
import collect.xyd.net.cn.xinyidaicollect.entity.User;
import collect.xyd.net.cn.xinyidaicollect.json.JsonUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.CoorConvert;
import collect.xyd.net.cn.xinyidaicollect.utils.SPUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public abstract class BusinessListFragment extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2, HttpRequestUtil.HttpRequestListener {
    protected PullToRefreshListView pullToRefresh;
    protected BaseAdapter adapter;
    protected Dialog dialog;
    private View view;
    protected ListView mListView;
    protected List<BusinessInfo> businesses = new LinkedList<>();
    protected SetBusinessDataListener setBusinessDataListener;
    public interface SetBusinessDataListener{
        void setDataByTag(String tabTag);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.pulltorefresh_list_fragment, container, false);
            pullToRefresh = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);
            mListView = pullToRefresh.getRefreshableView();
            mListView.setCacheColorHint(getResources().getColor(android.R.color.transparent));
            adapter = new BusinessAdapter(getActivity(), businesses);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(this);
            //pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
            pullToRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            pullToRefresh.setOnRefreshListener(this);
        } else {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        return view;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(getActivity(),
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        // Update the LastUpdatedLabel
        refreshView.getLoadingLayoutProxy()
                .setLastUpdatedLabel(label);
        // Do work to refresh the list here.
        String lat = (String)SPUtils.get(getActivity(), "lat", "");
        String lng = (String)SPUtils.get(getActivity(),"lng","");
        double geoLat = Double.valueOf(lat);
        double geoLng = Double.valueOf(lng);
        if(geoLat != 0.0 && geoLng != 0.0){
            Coor bdCoor = CoorConvert.bd_encrypt(geoLat, geoLng);
            Map params = new HashMap();
            params.put("gps_x", bdCoor.getLat() + "");
            params.put("gps_y", bdCoor.getLng() + "");
            new HttpRequestUtil(this).stringRequest(Constants.GET_CAR_BUSINESS, RequestUri.GET_CAR_BIZ, params);
        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(getActivity(),
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        // Update the LastUpdatedLabel
        refreshView.getLoadingLayoutProxy()
                .setLastUpdatedLabel(label);
        // Do work to refresh the list here.
        double geoLat = Double.valueOf(SPUtils.get(getActivity(), "lat", 0.0).toString());
        double geoLng = Double.valueOf(SPUtils.get(getActivity(),"lng",0.0).toString());
        if(geoLat != 0.0 && geoLng != 0.0){
            Coor bdCoor = CoorConvert.bd_encrypt(geoLat, geoLng);
            Map params = new HashMap();
            params.put("gps_x", bdCoor.getLat() + "");
            params.put("gps_y", bdCoor.getLng() + "");
            new HttpRequestUtil((MainActivity)getActivity()).stringRequest(Constants.GET_CAR_BUSINESS, RequestUri.GET_CAR_BIZ, params);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (((App) getActivity().getApplication()).user != null) {
            if(Integer.valueOf(businesses.get(position-1).getDistance())<=550) {
                Intent intent = new Intent(getActivity(), CollectActivity.class);
                intent.putExtra("type", businesses.get(position - 1).getType());
                intent.putExtra("agent_id", businesses.get(position - 1).getAgent_id());
                startActivity(intent);
            }else{
                T.showShort(getActivity(), "您目前距离该商家超过500米，请到商家附近进行操作");
            }
        } else {
            T.showShort(getActivity(), "请先登录");
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), MainActivity.LOGIN_REQUEST);
        }
    }

    public abstract void onResult(List<BusinessInfo> businesses);
    @Override
    public void onResponse(int requestCode, String data) {
        refreshComplete();
        dismissDialog();
        try {
            String resultData = ((BaseActivity)getActivity()).replaceSpecialtyStr(new String(data.getBytes("ISO-8859-1"), "GBK"),null,"");
            JsonUtil<List<BusinessInfo>> jsonUtil = new JsonUtil<List<BusinessInfo>>();
            RequestResult<List<BusinessInfo>> requestResult = jsonUtil.json2Obj(resultData, new TypeReference<RequestResult<List<BusinessInfo>>>() {
            });
            if (requestResult != null) {
                switch (requestResult.getMsg().getResultCode()) {
                    case 200:
                        onResult(requestResult.getData());
                        break;
                    case 400:
                        T.showShort(getActivity(), new String(requestResult.getMsg().getMessage()));
                        break;
                }
            } else {
                T.showShort(getActivity(), "服务器响应错误");
            }
        } catch (UnsupportedEncodingException e) {
            T.showShort(getActivity(), "服务器响应错误");
        }
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog=null;
    }
    private void refreshComplete(){
        if(pullToRefresh!=null && pullToRefresh.isRefreshing()) {
            pullToRefresh.onRefreshComplete();
        }
    }
    @Override
    public void onErrorResponse(int requestCode, String error) {
        refreshComplete();
        dismissDialog();
        switch (requestCode) {
            case Constants.GET_CAR_INFO:
                T.showShort(getActivity(), error);
                break;
        }
    }
}
