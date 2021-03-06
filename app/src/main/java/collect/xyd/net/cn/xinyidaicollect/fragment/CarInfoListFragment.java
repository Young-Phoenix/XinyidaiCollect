package collect.xyd.net.cn.xinyidaicollect.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import collect.xyd.net.cn.xinyidaicollect.ModifyActivity;
import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.ShowInfoActivity;
import collect.xyd.net.cn.xinyidaicollect.adapter.CarInfoAdapter;
import collect.xyd.net.cn.xinyidaicollect.entity.CarInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.RequestResult;
import collect.xyd.net.cn.xinyidaicollect.json.JsonUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.DialogUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.L;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/8/6 0006.
 */
public class CarInfoListFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2, HttpRequestUtil.HttpRequestListener, AdapterView.OnItemClickListener {
    protected PullToRefreshListView pullToRefresh;
    protected Dialog dialog;
    private int type = 0;
    private CarInfoAdapter carInfoAdapter;
    private List<CarInfo> not_conformed_cars = new LinkedList<CarInfo>();
    private List<CarInfo> not_passed_cars = new LinkedList<CarInfo>();
    private List<CarInfo> conformed_cars = new LinkedList<CarInfo>();
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        type = getArguments().getInt(Constants.TYPE);
        if (view == null) {
            view = inflater.inflate(R.layout.pulltorefresh_list_fragment, container, false);
            pullToRefresh = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);
            ListView mListView = pullToRefresh.getRefreshableView();
            carInfoAdapter = new CarInfoAdapter(getActivity());
            mListView.setAdapter(carInfoAdapter);
            mListView.setOnItemClickListener(this);
            pullToRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            pullToRefresh.setOnRefreshListener(this);
        } else {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        return view;
    }


    public void showDialog(Context context) {
        if (dialog == null) {
            dialog = DialogUtil.createLoadingDialog(context, "加载中……");
            dialog.show();
        } else if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog=null;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private final int NOT_CONFORMED = 0;
    private final int NOT_PASSED = 3;
    private final int CONFORMED = 1;
    private int carinfo_status = 0;

    private void initData() {
        switch (type) {
            case 0:
                if (not_conformed_cars.size() == 0) {
                    carinfo_status = NOT_CONFORMED;
                    loadData(Constants.GET_CAR_INFO);
                }
                break;
            case 1:
                if (not_passed_cars.size() == 0) {
                    carinfo_status = NOT_PASSED;
                    loadData(Constants.GET_CAR_INFO);
                }
                break;
            case 2:
                if (conformed_cars.size() == 0) {
                    carinfo_status = CONFORMED;
                    loadData(Constants.GET_CAR_INFO);
                }
                break;
        }
    }

    protected void loadData(int requestCode) {
        showDialog(getActivity());
        Map params = new HashMap();
        params.put(Constants.TOKEN, ((App) getActivity().getApplication()).user.getToken());
        params.put(Constants.INFO_STATUS, carinfo_status + "");
        new HttpRequestUtil(this).stringRequest(requestCode, RequestUri.GET_CAR_INFO, params);
    }

    protected void resultData(List<CarInfo> cars) {
        switch (type) {
            case 0:
                not_conformed_cars = cars;
                break;
            case 1:
                not_passed_cars = cars;
                break;
            case 2:
                conformed_cars = cars;
                break;
        }
        carInfoAdapter.setCars(cars);
        carInfoAdapter.notifyDataSetChanged();
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
        loadData(Constants.REFRESH_CAR_INFO);
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
    }

    protected void refreshComplete() {
        if (pullToRefresh != null && pullToRefresh.isRefreshing()) {
            pullToRefresh.onRefreshComplete();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(type!=2) {
            Intent intent = new Intent(getActivity(), ShowInfoActivity.class);
            CarInfo carInfo = null;
            switch (type) {
                case 0:
                    carInfo = not_conformed_cars.get(position - 1);
                    break;
                case 1:
                    carInfo = not_passed_cars.get(position - 1);
                    break;
                case 2:
                    carInfo = conformed_cars.get(position - 1);
                    break;
            }
            intent.putExtra(Constants.TYPE, ModifyActivity.CAR_TYPE);
            intent.putExtra(Constants.INFO_TYPE, type);
            intent.putExtra(Constants.INFO, carInfo);
            startActivity(intent);
        }
    }

    @Override
    public void onResponse(int requestCode, String data) {
        refreshComplete();
        dismissDialog();
        try {
            String resultData = ((BaseActivity)getActivity()).replaceSpecialtyStr(new String(data.getBytes("ISO-8859-1"), "GBK"),null,"");
            L.d(resultData);
            JsonUtil<List<CarInfo>> jsonUtil = new JsonUtil<List<CarInfo>>();
            RequestResult<List<CarInfo>> requestResult = jsonUtil.json2Obj(resultData, new TypeReference<RequestResult<List<CarInfo>>>() {
            });
            if (requestResult != null) {
                switch (requestResult.getMsg().getResultCode()) {
                    case 200:
                        switch (requestCode) {
                            case Constants.GET_CAR_INFO:
                                resultData(requestResult.getData());
                                break;
                            case Constants.REFRESH_CAR_INFO:
                                if (requestResult.getData().size() > 0) {
                                    resultData(requestResult.getData());
                                }
                                break;
                        }
                        break;
                    case 400:
                        if (requestCode == Constants.REFRESH_CAR_INFO)
                            T.showShort(getActivity(), requestResult.getMsg().getMessage());
                        break;
                }
            } else {
                T.showShort(getActivity(), "服务器响应错误");
            }
        } catch (UnsupportedEncodingException e) {
            T.showShort(getActivity(), "服务器响应错误");
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
