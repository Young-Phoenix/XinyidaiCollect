package collect.xyd.net.cn.xinyidaicollect;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Point;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.impl.client.DefaultHttpClient;

import java.util.Map;
import java.util.UUID;

import collect.xyd.net.cn.xinyidaicollect.entity.User;
import collect.xyd.net.cn.xinyidaicollect.receiver.NetWorkReceiver;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.L;
import collect.xyd.net.cn.xinyidaicollect.utils.NetUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.SPUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/7/15 0015.
 */
public class App extends Application {
    public RequestQueue mQueue;
    public Point point;
    //public AsyncHttpClient client;
    public User user = null;
    public String uniqueDeviceId = null;
    @Override
    public void onCreate() {
        super.onCreate();
        //是否打印日志
        L.isDebug=false;
        // volley初始化配置
        mQueue = Volley.newRequestQueue(this);
        // imageloader初始化配置
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        ImageLoader.getInstance().init(configuration);
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        point = new Point();
        wm.getDefaultDisplay().getSize(point);
        //client = new AsyncHttpClient();
        String username = String.valueOf(SPUtils.get(getApplicationContext(), Constants.USERNAME, ""));
        String token = String.valueOf(SPUtils.get(getApplicationContext(), Constants.TOKEN, ""));
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(token)) {
            user = new User(username, token);
        }
        uniqueDeviceId = (String)SPUtils.get(getApplicationContext(),Constants.UNIQUEDEVICEID,"");
        if(TextUtils.isEmpty(uniqueDeviceId)){
            uniqueDeviceId =getUniqueDeviceId();
            SPUtils.put(this,Constants.UNIQUEDEVICEID,uniqueDeviceId);
        }
        /*token = (String)SPUtils.get(this,Constants.UNIQUEDEVICEID,"");
        if(token != null){
            //发送到服务端验证token
        }*/

        if(!NetUtils.isConnected(this)){
            T.showLong(this,R.string.net_not_connect);
        }
    }


    //获取设备的唯一ID
    private String getUniqueDeviceId(){
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

}
