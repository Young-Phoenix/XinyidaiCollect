package collect.xyd.net.cn.xinyidaicollect.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/6 0006.
 */
public class AudioInfo implements Serializable{
    private int audio_id;
    private int che_id;
    private String audio_path;

    public int getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(int audio_id) {
        this.audio_id = audio_id;
    }

    public int getChe_id() {
        return che_id;
    }

    public void setChe_id(int che_id) {
        this.che_id = che_id;
    }

    public String getAudio_path() {
        return audio_path;
    }

    public void setAudio_path(String audio_path) {
        this.audio_path = audio_path;
    }
}
