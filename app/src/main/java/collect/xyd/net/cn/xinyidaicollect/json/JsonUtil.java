package collect.xyd.net.cn.xinyidaicollect.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import collect.xyd.net.cn.xinyidaicollect.entity.RequestResult;

/**
 * Created by Administrator on 2015/7/23 0023.
 */
public class JsonUtil<T> implements JSONInterface<RequestResult<T>>{
    private ObjectMapper objMapper;

    public JsonUtil() {
        if (objMapper == null) {
            objMapper = new ObjectMapper();
        }
    }
    @Override
    public RequestResult<T> json2Obj(String json, TypeReference<RequestResult<T>> valueType) {
        try {
            return objMapper.readValue(json, valueType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
