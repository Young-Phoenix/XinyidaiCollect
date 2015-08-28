package collect.xyd.net.cn.xinyidaicollect.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import collect.xyd.net.cn.xinyidaicollect.MainActivity;
import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.utils.NetUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/7/24 0024.
 */
public class NetWorkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(TextUtils.equals(action, MainActivity.CONNECTIVITY_CHANGE_ACTION)) {//网络变化的时候会发送通知
            if(!NetUtils.isConnected(context)){
                T.showLong(context, R.string.net_not_connect);
            }
        }
    }
}
