package collect.xyd.net.cn.xinyidaicollect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/4 0004.
 */
public class HouseInfoV2 implements Serializable{
    private int fang_id;
    private int agent_id;
    private int user_id;
    private String title;
    private String content;
    private String address;
    private String tel;
    private String time;
    private String ip;
    private int status;
    private int cate;//1.租赁；2.二手房
    private double shoujia;
    private String fangdai;
    private String huxing;
    private String gaikuang;
    private String leixing;
    private String zhuangxiu;
    private String chaox;
    private int chanquan;
    private String niandai;
    private String lianxiren;
    private String xiaoqu_describe;
    private String fangyuan_descirbe;
    private String jiaotong;
    private double mianji;
    private String province;
    private String city;
    private String area;
    private List<HouseImageInfo> images;
    private Map<String,HouseVideoInfo> video;
    @JsonIgnore
    private int is_tuijian;
    @JsonIgnore
    private double gps_x;
    @JsonIgnore
    private double gps_y;

    public HouseInfoV2(){}
    public HouseInfoV2(String time, String address, String title, String huxing, double shoujia) {
        this.time = time;
        this.address = address;
        this.title = title;
        this.huxing = huxing;
        this.shoujia = shoujia;

    }

    public int getFang_id() {
        return fang_id;
    }

    public void setFang_id(int fang_id) {
        this.fang_id = fang_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCate() {
        return cate;
    }

    public void setCate(int cate) {
        this.cate = cate;
    }

    public double getShoujia() {
        return shoujia;
    }

    public void setShoujia(double shoujia) {
        this.shoujia = shoujia;
    }

    public String getFangdai() {
        return fangdai;
    }

    public void setFangdai(String fangdai) {
        this.fangdai = fangdai;
    }

    public String getHuxing() {
        return huxing;
    }

    public void setHuxing(String huxing) {
        this.huxing = huxing;
    }

    public String getGaikuang() {
        return gaikuang;
    }

    public void setGaikuang(String gaikuang) {
        this.gaikuang = gaikuang;
    }

    public String getLeixing() {
        return leixing;
    }

    public void setLeixing(String leixing) {
        this.leixing = leixing;
    }

    public String getZhuangxiu() {
        return zhuangxiu;
    }

    public void setZhuangxiu(String zhuangxiu) {
        this.zhuangxiu = zhuangxiu;
    }

    public String getChaox() {
        return chaox;
    }

    public void setChaox(String chaox) {
        this.chaox = chaox;
    }

    public int getChanquan() {
        return chanquan;
    }

    public void setChanquan(int chanquan) {
        this.chanquan = chanquan;
    }

    public String getNiandai() {
        return niandai;
    }

    public void setNiandai(String niandai) {
        this.niandai = niandai;
    }

    public String getLianxiren() {
        return lianxiren;
    }

    public void setLianxiren(String lianxiren) {
        this.lianxiren = lianxiren;
    }

    public String getFangyuan_descirbe() {
        return fangyuan_descirbe;
    }

    public void setFangyuan_descirbe(String fangyuan_descirbe) {
        this.fangyuan_descirbe = fangyuan_descirbe;
    }

    public String getJiaotong() {
        return jiaotong;
    }

    public void setJiaotong(String jiaotong) {
        this.jiaotong = jiaotong;
    }

    public double getMianji() {
        return mianji;
    }

    public void setMianji(double mianji) {
        this.mianji = mianji;
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

    public List<HouseImageInfo> getImages() {
        return images;
    }

    public void setImages(List<HouseImageInfo> images) {
        this.images = images;
    }

    public String getXiaoqu_describe() {
        return xiaoqu_describe;
    }

    public void setXiaoqu_describe(String xiaoqu_describe) {
        this.xiaoqu_describe = xiaoqu_describe;
    }

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public int getIs_tuijian() {
        return is_tuijian;
    }

    public void setIs_tuijian(int is_tuijian) {
        this.is_tuijian = is_tuijian;
    }

    public double getGps_x() {
        return gps_x;
    }

    public void setGps_x(double gps_x) {
        this.gps_x = gps_x;
    }

    public double getGps_y() {
        return gps_y;
    }

    public void setGps_y(double gps_y) {
        this.gps_y = gps_y;
    }

    public Map<String, HouseVideoInfo> getVideo() {
        return video;
    }

    public void setVideo(Map<String, HouseVideoInfo> video) {
        this.video = video;
    }
}
