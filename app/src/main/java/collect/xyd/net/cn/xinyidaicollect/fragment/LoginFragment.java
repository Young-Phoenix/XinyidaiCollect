package collect.xyd.net.cn.xinyidaicollect.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.Pattern;

import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/7/16 0016.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditText username, password;
    private Button login;
    private Button close;
    private String regEX = "^[a-zA-Z_][a-zA-Z0-9_]{2,15}$";
    private LoginFragCallBack callBack;
    private Pattern pattern = Pattern.compile(regEX, Pattern.CASE_INSENSITIVE);

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (!TextUtils.isEmpty(username.getText()) && !TextUtils.isEmpty(password.getText())) {
                    if (pattern.matcher(username.getText()).find()) {
                        callBack.login(username.getText().toString(), password.getText().toString());
                    } else {
                        T.showLong(getActivity(), "无效的用户名");
                    }
                }else{
                    T.showLong(getActivity(), "用户名和密码不能为空");
                }
                break;
            case R.id.iv_close_button:
                callBack.close();
                break;
        }
    }

    public interface LoginFragCallBack {
        void login(String username, String password);

        void close();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callBack = (LoginFragCallBack) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + "must implements LoginFragCallBack");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, null);
        username = (EditText) view.findViewById(R.id.et_username);
        password = (EditText) view.findViewById(R.id.et_password);
        login = (Button) view.findViewById(R.id.btn_login);
        close = (Button) view.findViewById(R.id.iv_close_button);
        initListener();
        return view;
    }

    private void initListener() {
        login.setOnClickListener(this);
        close.setOnClickListener(this);
    }
}
