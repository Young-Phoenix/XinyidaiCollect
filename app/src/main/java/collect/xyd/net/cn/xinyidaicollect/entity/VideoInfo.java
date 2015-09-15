package collect.xyd.net.cn.xinyidaicollect.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/6 0006.
 */
public class VideoInfo extends BaseVideo{
    private int che_id;
    private int type;


    public int getChe_id() {
        return che_id;
    }

    public void setChe_id(int che_id) {
        this.che_id = che_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
