package collect.xyd.net.cn.xinyidaicollect.utils;

import android.text.TextUtils;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/8/7 0007.
 */
public class TimeUtils {

    public static String longToStrng(String time) {
        Date date;
        if (TextUtils.isEmpty(time)) {
            return "";
        } else {
            try {
                date = new Date(Long.parseLong(time.trim()) * 1000);
            }catch (NumberFormatException e){
                return time;
            }
        }

        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        System.out.println("TIME:::" + dateString);
        return dateString;
    }
}
