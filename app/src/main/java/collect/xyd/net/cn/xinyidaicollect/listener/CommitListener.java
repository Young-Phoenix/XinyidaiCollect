package collect.xyd.net.cn.xinyidaicollect.listener;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/4 0004.
 */
public interface CommitListener {
    void commit(String uri, Map<String, String> params, Map<String, File> fileMap);
}
