package collect.xyd.net.cn.xinyidaicollect.json;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Created by Administrator on 2015/7/23 0023.
 */
public interface JSONInterface<Y> {
    //json转成对象
    public Y json2Obj(String json,TypeReference<Y> valueType);
}
