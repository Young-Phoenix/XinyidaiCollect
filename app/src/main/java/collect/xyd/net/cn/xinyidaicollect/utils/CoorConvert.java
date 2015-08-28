package collect.xyd.net.cn.xinyidaicollect.utils;

import collect.xyd.net.cn.xinyidaicollect.entity.Coor;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class CoorConvert {
    // 将 GCJ-02 坐标转换成 BD-09 坐标
    public static Coor bd_encrypt(double gg_lat, double gg_lon)
    {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        //经度，纬度
        return new Coor(bd_lat,bd_lon);
    }
    // BD-09 将 坐标转换成 GCJ-02 坐标
    public static Coor bd_decrypt(double bd_lat, double bd_lon)
    {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        //经度，纬度
        return new Coor(gg_lat,gg_lon);
    }
}
