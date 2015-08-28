package collect.xyd.net.cn.xinyidaicollect.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/7 0007.
 */
public class BaseImage implements Serializable{
    protected int img_id;
    protected String img_path;

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
