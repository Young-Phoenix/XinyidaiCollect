package collect.xyd.net.cn.xinyidaicollect.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/6 0006.
 */
public class HouseVideoInfo implements Serializable{
    private int video_id;
    private int fang_id;
    private String video_path;
    private int type;

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public int getFang_id() {
        return fang_id;
    }

    public void setFang_id(int fang_id) {
        this.fang_id = fang_id;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
