package collect.xyd.net.cn.xinyidaicollect.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/4 0004.
 */
public class CarInfo implements Serializable{
    private int che_id;//主键
    private int agent_id;//车商外键
    private int user_id;//发布人id
    private String title;//标题
    private String che_type;//车辆类型
    //private int cheling;//车龄
    private String cheling;//车龄
    private double shoujia;//售价
    private String pinpai;//品牌车系
    private String address;//地址
    private String tel;//电话
    private String lianxiren;//联系人
    private String shangpaitime;//上牌时间
    private double licheng;//里程
    private double pailiang;//排量
    private String color;//颜色
    private String nianjian_endtime;//年检到期时间
    private String qiangxian;//强险到期时间
    private String shigu;//事故车辆
    private String detail_description;//详情描述
    private String time;//添加时间
    private int status;//状态 0未审核，1审核通过，2初审通过，3不通过
    @JsonIgnore
    private String ip;
    private String province;//省
    private String city;//市
    private String area;//县
    private String remark;//备注
    private List<CarImageInfo> images;
    private Map<String,VideoInfo> video;
    private AudioInfo audio;
    @JsonIgnore
    private int is_tuijian;
    public CarInfo(){

    }
    public CarInfo(String time, double licheng, String title, String pinpai, double shoujia) {
        this.time = time;
        this.licheng = licheng;
        this.title = title;
        this.pinpai = pinpai;
        this.shoujia = shoujia;
    }

    public int getChe_id() {
        return che_id;
    }

    public void setChe_id(int che_id) {
        this.che_id = che_id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChe_type() {
        return che_type;
    }

    public void setChe_type(String che_type) {
        this.che_type = che_type;
    }

    public String getCheling() {
        return cheling;
    }

    public void setCheling(String cheling) {
        this.cheling = cheling;
    }

    public double getShoujia() {
        return shoujia;
    }

    public void setShoujia(double shoujia) {
        this.shoujia = shoujia;
    }

    public String getPinpai() {
        return pinpai;
    }

    public void setPinpai(String pinpai) {
        this.pinpai = pinpai;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLianxiren() {
        return lianxiren;
    }

    public void setLianxiren(String lianxiren) {
        this.lianxiren = lianxiren;
    }

    public String getShangpaitime() {
        return shangpaitime;
    }

    public void setShangpaitime(String shangpaitime) {
        this.shangpaitime = shangpaitime;
    }

    public double getLicheng() {
        return licheng;
    }

    public void setLicheng(double licheng) {
        this.licheng = licheng;
    }

    public double getPailiang() {
        return pailiang;
    }

    public void setPailiang(double pailiang) {
        this.pailiang = pailiang;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNianjian_endtime() {
        return nianjian_endtime;
    }

    public void setNianjian_endtime(String nianjian_endtime) {
        this.nianjian_endtime = nianjian_endtime;
    }

    public String getQiangxian() {
        return qiangxian;
    }

    public void setQiangxian(String qiangxian) {
        this.qiangxian = qiangxian;
    }

    public String getShigu() {
        return shigu;
    }

    public void setShigu(String shigu) {
        this.shigu = shigu;
    }

    public String getDetail_description() {
        return detail_description;
    }

    public void setDetail_description(String detail_description) {
        this.detail_description = detail_description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<CarImageInfo> getImages() {
        return images;
    }

    public void setImages(List<CarImageInfo> images) {
        this.images = images;
    }

    public Map<String, VideoInfo> getVideo() {
        return video;
    }

    public void setVideo(Map<String, VideoInfo> video) {
        this.video = video;
    }

    public AudioInfo getAudio() {
        return audio;
    }

    public void setAudio(AudioInfo audio) {
        this.audio = audio;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getIs_tuijian() {
        return is_tuijian;
    }

    public void setIs_tuijian(int is_tuijian) {
        this.is_tuijian = is_tuijian;
    }
}
