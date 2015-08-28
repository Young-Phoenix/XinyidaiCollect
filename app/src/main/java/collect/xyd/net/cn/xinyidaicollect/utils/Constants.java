package collect.xyd.net.cn.xinyidaicollect.utils;

import com.amap.api.maps.model.LatLng;

/**
 * Created by Administrator on 2015/7/15 0015.
 */
public class Constants {
    public static final String SERVER_IP = "http://www.xyd.net.cn/app.php/";
    public static final String SERVER_CAR_IP = "http://www.xyd.net.cn/Uploads/che_info/";
    public static final String SERVER_HOUSE_IP = "http://www.xyd.net.cn/Uploads/fang_info/";
//    public static final String SERVER_IP = "http://ceshi.xyd.net.cn/app.php/";
//    public static final String SERVER_CAR_IP = "http://ceshi.xyd.net.cn/Uploads/che_info/";
//    public static final String SERVER_HOUSE_IP = "http://ceshi.xyd.net.cn/Uploads/fang_info/";
    //public static final String SERVER_IP = ""http://192.168.0.150:8080/qianyouba_app_server/servlet/UploadServlet"";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String UNIQUEDEVICEID = "unique_device_id";
    public static final String TOKEN = "token";
    public static final String INFO_STATUS= "status";
    public static final String BUSINESSID = "agent_id";
    public static final String GEOLAT = "gps_x";
    public static final String GEOLNG = "gps_y";
    public static final int MAP_FRAGMENT = 1;
    public static final int LOGIN_REQUEST = 1001;
    public static final int GET_CAR_BUSINESS = 1002;
    public static final int GET_CAR_INFO = 1003;
    public static final int REFRESH_CAR_INFO = 1004;
    public static final int GET_INFO = 1005;
    public static final int REFRESH_INFO = 1006;
    public static final int DELETE_IMAGE= 1007;
    public static final int DELETE_VIDEO = 1008;
    public static final int DELETE_AUDIO = 1009;
    public static final LatLng XINXIANG1 = new LatLng(35.286825,113.901186);
    public static final LatLng XINXIANG2 = new LatLng(35.28404, 113.901604);

    public static final String TYPE = "type";
    public static final String INFO_TYPE = "info_type";
    public static final String INFO = "info";

    public static final int ADD = 1;
    public static final int MODIFY = 2;
}
