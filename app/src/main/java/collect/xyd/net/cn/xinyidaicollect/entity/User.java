package collect.xyd.net.cn.xinyidaicollect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/16 0016.
 */
//用户类
public class User implements Serializable {
    @JsonIgnore
    private String username;
    @JsonIgnore
    private String password;
    @JsonProperty("token")
    private String token;
    private double geoLat;//纬度
    private double geoLng;//经度
    public User(){

    }

    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public User(String username, String password, String token, double geoLat, double geoLng) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.geoLat = geoLat;
        this.geoLng = geoLng;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(double geoLat) {
        this.geoLat = geoLat;
    }

    public double getGeoLng() {
        return geoLng;
    }

    public void setGeoLng(double geoLng) {
        this.geoLng = geoLng;
    }
}
