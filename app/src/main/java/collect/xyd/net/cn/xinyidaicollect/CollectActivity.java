package collect.xyd.net.cn.xinyidaicollect;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import collect.xyd.net.cn.xinyidaicollect.entity.RequestResult;
import collect.xyd.net.cn.xinyidaicollect.entity.User;
import collect.xyd.net.cn.xinyidaicollect.fragment.CarInfoFragment;
import collect.xyd.net.cn.xinyidaicollect.fragment.CollectInfoFragment;
import collect.xyd.net.cn.xinyidaicollect.fragment.HouseInfoFragment;
import collect.xyd.net.cn.xinyidaicollect.fragment.HouseInfoFragmentV2;
import collect.xyd.net.cn.xinyidaicollect.fragment.ShopBusinessFragment;
import collect.xyd.net.cn.xinyidaicollect.json.JsonUtil;
import collect.xyd.net.cn.xinyidaicollect.listener.CommitListener;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.DialogUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.L;
import collect.xyd.net.cn.xinyidaicollect.utils.MultipartUtility;
import collect.xyd.net.cn.xinyidaicollect.utils.PictureUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.T;


/**
 * Created by Administrator on 2015/7/16 0016.
 */
public class CollectActivity extends BaseActivity implements View.OnClickListener, CommitListener {
    public static final int REQUEST_SOCKET_TIMEOUT_MS = 50 * 1000;
    protected Dialog dialog;
    protected FragmentTransaction transaction;
    public static final String HOUSEINFO_TAG = "houseinfo";
    public static final String CARINFO_TAG = "carinfo";
    public static final String SHOPINFO_TAG = "shopinfo";
    public static final int HOUSE_TYPE = 1;
    public static final int CAR_TYPE = 2;
    public static final int SHOP_INFO_TYPE = 3;
    protected int info_type;//1.房产；2.车商;3.商铺
    private int agent_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.collect_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        info_type = getIntent().getExtras().getInt("type");
        agent_id = getIntent().getExtras().getInt("agent_id");
        if (info_type == HOUSE_TYPE) {
            getActionBar().setTitle("添加房产信息");
            transaction = getSupportFragmentManager().beginTransaction();
            //transaction.replace(R.id.fl_content, new HouseInfoFragment(), HOUSEINFO_TAG);
            transaction.replace(R.id.fl_content, new HouseInfoFragmentV2(), HOUSEINFO_TAG);
            transaction.commit();
        } else if (info_type == CAR_TYPE) {
            getActionBar().setTitle("添加车辆信息");
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_content, new CarInfoFragment(), CARINFO_TAG);
            transaction.commit();
        } else if(info_type == SHOP_INFO_TYPE){
            getActionBar().setTitle("添加店铺信息");
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_content, new ShopBusinessFragment(), SHOPINFO_TAG);
            transaction.commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //提交信息
    @Override
    public void commit(String uri, Map<String, String> params,
                       final Map<String, File> fileMap) {
        if(params!=null) {
            params.put(Constants.UNIQUE_DEVICE_ID, app.uniqueDeviceId);
            if(agent_id!=0) {
                params.put(Constants.BUSINESSID, agent_id + "");
            }
            if (app.user != null) {
                params.put(Constants.TOKEN, app.user.getToken());
                params.put(Constants.GEOLAT, app.user.getGeoLat() + "");
                params.put(Constants.GEOLNG, app.user.getGeoLng() + "");
            }else{
                return;
            }
        }
        /*MultipartRequest multipartRequest = new MultipartRequest(Constants.SERVER_IP + uri
                , params, fileMap, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                dialogDismiss();
                L.d("success", response.toString());
                //删除缩小后的图片文件
                for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                    if (entry.getKey().startsWith("image")) {
                        PictureUtil.deleteTempFile(entry.getValue().getAbsolutePath());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogDismiss();
                if (error != null) {
                    StringBuffer errorMsg = new StringBuffer("请求失败");
                    if (error instanceof NetworkError) {
                        errorMsg.append(",网络异常");
                    } else if (error instanceof ParseError) {
                        errorMsg.append(",解析错误");
                    } else if (error instanceof ServerError) {
                        errorMsg.append(",服务器异常");
                    } else if (error instanceof TimeoutError) {
                        errorMsg.append(",连接超时");
                    }
                    T.showShort(CollectActivity.this, errorMsg.toString());
                    error.printStackTrace();
                    L.d("error", "msg=" + error.getMessage());
                    if (error.networkResponse != null)
                        L.d("resultcode", "resultcode=" + error.networkResponse.statusCode);

                }

            }
        });
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        app.mQueue.add(multipartRequest);*/
        Map<String, String> base_config = new HashMap<String,String>();
        base_config.put("url", Constants.SERVER_IP + uri);
        base_config.put("charset", "GBK");
        Map<String, Map> reqParams = new HashMap<String, Map>();
        reqParams.put("base_config", base_config);
        reqParams.put("form_field", params);
        if(fileMap!=null) {
            reqParams.put("file_part", fileMap);
        }
        new UploadUtil().execute(reqParams);
    }

    private class UploadUtil extends AsyncTask<Map<String, Map>, Integer, List> {
        @Override
        protected List doInBackground(Map<String, Map>... params) {
            Map<String, String> base_config = params[0].get("base_config");
            Map<String, String> form_field = params[0].get("form_field");
            Map<String, File> file_part = params[0].get("file_part");
            MultipartUtility multipart = null;
            try {
                multipart = new MultipartUtility(base_config.get("url"), base_config.get("charset"));
                multipart.addHeaderField("User-Agent", "WinHttpClient");
                multipart.addHeaderField("Test-Header", "Header-Value");
                for (Map.Entry<String, String> entry : form_field.entrySet()) {
                    multipart.addFormField(entry.getKey(), entry.getValue());
                }
                if(file_part!=null) {
                    for (Map.Entry<String, File> entry : file_part.entrySet()) {
                        multipart.addFilePart(entry.getKey(), entry.getValue());
                    }
                }
                List<String> response = multipart.finish();
                StringBuilder result = new StringBuilder();
                for (String msg : response) {
                    result.append(msg);
                }
                List resultList = new ArrayList();
                resultList.add(result.toString());
                resultList.add(file_part);
                return resultList;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
           showDialog(CollectActivity.this,"正在上传……");
        }

        @Override
        protected void onPostExecute(List resultList) {
            dismissDialog();
            if (resultList!=null && resultList.size() > 0) {
                JsonUtil<User> jsonUtil = new JsonUtil<User>();
                RequestResult<User> commitResult = jsonUtil.json2Obj(resultList.get(0).toString(), new TypeReference<RequestResult<User>>() {
                });
                if(commitResult!=null) {
                    switch (commitResult.getMsg().getResultCode()) {
                        case 200:
                            T.showShort(CollectActivity.this, commitResult.getMsg().getMessage());
                            if (resultList.get(1) != null) {
                                for (Map.Entry<String, File> entry : ((Map<String, File>) (resultList.get(1))).entrySet()) {
                                    if (entry.getKey().startsWith("image")) {
                                        PictureUtil.deleteTempFile(entry.getValue().getAbsolutePath());
                                    }
                                }
                            }
                            if (info_type == HOUSE_TYPE) {
                                transaction = getSupportFragmentManager().beginTransaction();
                                //transaction.replace(R.id.fl_content, new HouseInfoFragment(), CARINFO_TAG);
                                transaction.replace(R.id.fl_content, new HouseInfoFragmentV2(), CARINFO_TAG);
                                transaction.commit();
                            } else if (info_type == CAR_TYPE) {
                                transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fl_content, new CarInfoFragment(), CARINFO_TAG);
                                transaction.commit();
                            } else if (info_type == SHOP_INFO_TYPE) {
                                transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fl_content, new ShopBusinessFragment(), SHOPINFO_TAG);
                                transaction.commit();
                            }
                            break;
                        case 400:
                            T.showShort(CollectActivity.this, commitResult.getMsg().getMessage());
                            break;
                    }
                }else{
                    T.showShort(CollectActivity.this, "提交失败");
                }
            } else {
                T.showShort(CollectActivity.this, "连接超时，请重试");
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
