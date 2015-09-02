package collect.xyd.net.cn.xinyidaicollect;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.net.xyd.Test.PixColor;
import cn.net.xyd.area.CityModel;
import cn.net.xyd.area.DistrictModel;
import cn.net.xyd.area.ProvinceModel;
import cn.net.xyd.area.XmlParserHandler;
import collect.xyd.net.cn.xinyidaicollect.utils.DialogUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/7/16 0016.
 */
public abstract class BaseActivity extends FragmentActivity {
    protected App app;
    protected Dialog dialog;

    /**
     * 解析省市区的XML数据
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getApplication();
        //new PixColor(this).getPixColor();
    }

    protected void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

    protected void showDialog(Context context, String msg) {
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
        dialog = null;
        dialog = DialogUtil.createLoadingDialog(context, msg);
        dialog.show();

    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    protected void startActivity(boolean loginJudge, Class<?> cls, Bundle bundle) {
        if (loginJudge) {
            if (app.user != null) {
                startActivity(cls, bundle);
            } else {
                startLoginActivity(true, null);
            }
        } else {
            startActivity(cls, bundle);
        }
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     **/
    protected void startActivityForResult(boolean loginJudge, Class<?> cls, Bundle bundle, int requestCode) {
        if (loginJudge) {
            if (app.user != null) {
                startActivityForResult(cls, bundle, requestCode);
            } else {
                startLoginActivity(true, null);
            }
        } else {
            startActivityForResult(cls, bundle, requestCode);
        }
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 启动登录activity
     *
     * @param isShowToast
     * @param bundle
     */
    protected void startLoginActivity(boolean isShowToast, Bundle bundle) {
        if (isShowToast) {
            T.showShort(this, "请先登录");
        }
        startActivityForResult(LoginActivity.class, bundle, MainActivity.LOGIN_REQUEST);
    }

    public static boolean isBlankOrNull(String str) {
        if (null == str) return true;
        //return str.length()==0?true:false;
        return str.length() == 0;
    }


    /**
     * 替换字符串
     * @param str 输入字符串
     * @param pattern 正则表达式
     * @param replace 替换为
     * @return
     */
    public static String replaceSpecialtyStr(String str, String pattern, String replace) {
        if (isBlankOrNull(pattern))
            pattern = "\\s*|\t|\r|\n";//去除字符串中空格、换行、制表
        if (isBlankOrNull(replace))
            replace = "";
        return Pattern.compile(pattern).matcher(str).replaceAll(replace);

    }
}
