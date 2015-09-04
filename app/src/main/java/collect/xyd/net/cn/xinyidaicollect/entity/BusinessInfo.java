package collect.xyd.net.cn.xinyidaicollect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2015/7/23 0023.
 */
//车商信息
public class BusinessInfo {
    @JsonProperty("agent_id")
    private int agent_id;
    @JsonProperty("user_id")
    private int user_id;
    @JsonProperty("agent_name")
    private String agent_name;
    @JsonProperty("type")
    private int type;
    @JsonProperty("status")
    private int status;
    @JsonProperty("linkman")
    private String link_man;
    @JsonProperty("tel")
    private String tel;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("description")
    private String description;
    @JsonProperty("address")
    private String address;
    @JsonProperty("gps_y")
    private double gps_y;//经度
    @JsonProperty("gps_x")
    private double gps_x;//纬度
    @JsonProperty("addtime")
    private String add_time;
    @JsonProperty("addip")
    private String add_ip;
    @JsonProperty("distance")
    private String distance;//距离
    @JsonIgnore
    private String paixu;
    @JsonIgnore
    private String type_nid;
    public BusinessInfo() {

    }

    public BusinessInfo(String agent_name,String address,String tel,String distance){
        this.agent_name = agent_name;
        this.address = address;
        this.tel = tel;
        this.distance = distance;
    }

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLink_man() {
        return link_man;
    }

    public void setLink_man(String link_man) {
        this.link_man = link_man;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getGps_y() {
        return gps_y;
    }

    public void setGps_y(double gps_y) {
        this.gps_y = gps_y;
    }

    public double getGps_x() {
        return gps_x;
    }

    public void setGps_x(double gps_x) {
        this.gps_x = gps_x;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getAdd_ip() {
        return add_ip;
    }

    public void setAdd_ip(String add_ip) {
        this.add_ip = add_ip;
    }

    public String getPaixu() {
        return paixu;
    }

    public void setPaixu(String paixu) {
        this.paixu = paixu;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setType_nid(String type_nid) {
        this.type_nid = type_nid;
    }
}
