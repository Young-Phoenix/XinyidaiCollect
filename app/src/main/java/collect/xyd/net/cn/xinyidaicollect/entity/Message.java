package collect.xyd.net.cn.xinyidaicollect.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/23 0023.
 */
//消息类
public class Message implements Serializable{
    @JsonProperty("resultcode")
    private int resultCode;
    @JsonProperty("message")
    private String message;
public Message(){

}
    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
