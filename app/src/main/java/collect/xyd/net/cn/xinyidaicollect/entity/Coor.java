package collect.xyd.net.cn.xinyidaicollect.entity;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class Coor {
    private double lat;
    private double lng;

    public Coor(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
