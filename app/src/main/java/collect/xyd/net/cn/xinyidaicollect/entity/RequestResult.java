package collect.xyd.net.cn.xinyidaicollect.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/23 0023.
 */
//请求返回结果
public class RequestResult<T> implements Serializable {
    @JsonProperty("msg")
    private Message msg;
    @JsonProperty("data")
    private T data;

    public RequestResult(){

    }
    public Message getMsg() {
        return msg;
    }
    public void setMsg(Message msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
