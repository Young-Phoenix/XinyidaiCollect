package collect.xyd.net.cn.xinyidaicollect.utils;

import java.util.Comparator;

import collect.xyd.net.cn.xinyidaicollect.service.BusinessInfo;

/**
 * Created by Administrator on 2015/8/7 0007.
 */
public class BusinessInfoComparator implements Comparator<BusinessInfo> {
    @Override
    public int compare(BusinessInfo lhs, BusinessInfo rhs) {
        return Integer.valueOf(lhs.getDistance())-Integer.valueOf(rhs.getDistance());
    }
}
