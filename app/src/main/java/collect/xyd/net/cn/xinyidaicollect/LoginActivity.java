package collect.xyd.net.cn.xinyidaicollect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.net.xyd.http.HttpRequestUtil;
import cn.net.xyd.http.RequestUri;
import collect.xyd.net.cn.xinyidaicollect.entity.RequestResult;
import collect.xyd.net.cn.xinyidaicollect.entity.User;
import collect.xyd.net.cn.xinyidaicollect.fragment.LoginFragment;
import collect.xyd.net.cn.xinyidaicollect.json.JsonUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.DialogUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.SPUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/7/16 0016.
 */
public class LoginActivity extends BaseActivity implements LoginFragment.LoginFragCallBack, HttpRequestUtil.HttpRequestListener {
    private FragmentTransaction transaction;
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
    }

    private void initView() {
        transaction = getSupportFragmentManager().beginTransaction();
        loginFragment = new LoginFragment();
        transaction.replace(R.id.fl_content, loginFragment);
        transaction.commit();
    }

    @Override
    public void login(String username, String password) {
        showDialog(this,"登录ing……");
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.USERNAME, this.username = username);
        params.put(Constants.PASSWORD, password);
        //发送设备唯一id
        params.put(Constants.UNIQUE_DEVICE_ID, app.uniqueDeviceId);
        new HttpRequestUtil(this).stringRequest(Constants.LOGIN_REQUEST, RequestUri.LOGIN, params);
    }

    private String username;

    @Override
    public void close() {
        this.finish();
    }

    @Override
    public void onResponse(int requestCode, String data) {
        dismissDialog();
        try {
            String resultData = new String(data.getBytes("ISO-8859-1"), "GBK");
            JsonUtil<User> jsonUtil = new JsonUtil<User>();
            RequestResult<User> loginResult = jsonUtil.json2Obj(resultData, new TypeReference<RequestResult<User>>() {
            });
            if (loginResult != null) {
                switch (loginResult.getMsg().getResultCode()) {
                    case 200:
                        //登录成功
                        T.showShort(this, loginResult.getMsg().getMessage());
                        if (requestCode == Constants.LOGIN_REQUEST) {
                            String token = loginResult.getData().getToken();
                            SPUtils.put(this.getApplicationContext(), Constants.TOKEN, token);
                            SPUtils.put(this.getApplicationContext(), Constants.USERNAME, username);
                            app.user = new User(username, token);
                            this.finish();
                        }
                        break;
                    case 400:
                        T.showShort(this, loginResult.getMsg().getMessage());
                        break;
                }
            } else {
                T.showShort(this, "服务器响应错误");
            }
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
            T.showShort(this, "服务器响应错误");
        }


    }

    @Override
    public void onErrorResponse(int requestCode, String error) {
        dismissDialog();
        switch (requestCode) {
            case Constants.LOGIN_REQUEST:
                T.showShort(this, error);
                break;
        }
    }
}
