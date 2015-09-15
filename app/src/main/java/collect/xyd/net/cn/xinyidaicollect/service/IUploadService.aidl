// IUploadService.aidl

package collect.xyd.net.cn.xinyidaicollect.service;
import collect.xyd.net.cn.xinyidaicollect.service.Info;
// Declare any non-default types here with import statements

interface IUploadService {
    void upload(in Info info,in String uri);
}
