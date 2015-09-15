// IUploadService.aidl

package collect.xyd.net.cn.xinyidaicollect.service;
import collect.xyd.net.cn.xinyidaicollect.service.BusinessInfo;
// Declare any non-default types here with import statements

interface IUploadService {
    void upload(in BusinessInfo info,in String uri);
}
