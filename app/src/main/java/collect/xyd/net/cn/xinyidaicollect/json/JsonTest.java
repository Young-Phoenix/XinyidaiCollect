package collect.xyd.net.cn.xinyidaicollect.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import collect.xyd.net.cn.xinyidaicollect.entity.BusinessInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.HouseInfoV2;
import collect.xyd.net.cn.xinyidaicollect.entity.RequestResult;

/**
 * Created by Administrator on 2015/7/23 0023.
 */
public class JsonTest {
    public static void main(String[] arg){
        /*String data = "{\"msg\":{\"resultCode\":200,\"message\":\"登录成功\"},\"data\":{\"username\":\"张三\",\"token\":\"fdlajgldsjagadfja\"}}";
        String json = "{\"msg\": {\"resultcode\": \"200\",\"message\": \"列表获取成功\"},\"data\":[{\"agent_id\":\"1\",\"user_id\":\"3831\",\"agent_name\":\"北京新易贷投资顾问有限公司\",\"type\":\"2\",\"status\":\"1\",\"linkman\":\"周吉宏\",\"tel\":\"400-886-7860\",\"mobile\":\"18790696575\",\"description\":\" 新易贷拥有雄厚的资金背景，先进的管理体系，专业的运营团队，一流的用户体验，主要服务于借贷双方的个人及中小微企业，通过提供优质的平台服务达成投资方、借贷方共同受益的双赢局面。公司以“服务他人，成就自己”为发展理念，希望经过我们的专业程度和不懈努力，帮助更多的人和企业，完成自己的梦想。\",\"address\":\"河南省新乡市华兰大道509号创业园三号园南楼一楼西厅\",\"gps_y\":\"113.917278\",\"gps_x\":\"35.287357\",\"addtime\":\"1436408619\",\"addip\":\"127.0.0.1\",\"paixu\":\"0\"},{\"agent_id\":\"7\",\"user_id\":\"6613\",\"agent_name\":\"新易贷房产中介测试一\",\"type\":\"1\",\"status\":\"1\",\"linkman\":\"周\",\"tel\":\"400-886-7860\",\"mobile\":\"18790696575\",\"description\":\"新易贷房产中介描述内容测试一\",\"address\":\"牧野路\",\"gps_y\":\"113.918478\",\"gps_x\":\"35.289088\",\"addtime\":\"1437702574\",\"addip\":\"222.89.89.158\",\"paixu\":\"0\"}]}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            RequestResult<List<BusinessInfo>> loginResult = mapper.readValue(json, new TypeReference<RequestResult<List<BusinessInfo>>>() {});
            System.out.println(loginResult.getMsg().getResultCode()+":"+loginResult.getMsg().getMessage());
            System.out.println(loginResult.getData().get(0).getAgent_name() + ":" + loginResult.getData().get(0).getAddress());
            System.out.println(loginResult.getData().get(1).getAgent_name() + ":" + loginResult.getData().get(0).getAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        String json = "{\"msg\":{\"resultcode\":\"200\",\"message\":\"列表获取成功\"},\"data\":[{\"agent_id\":\"1\",\"user_id\":\"3559\",\"agent_name\":\"四季青房产\",\"type\":\"1\",\"status\":\"0\",\"linkman\":\"小艾\",\"tel\":\"15600006666\",\"mobile\":\"15600000000\",\"description\":\"这是一个设置的管理员账号\",\"address\":\"华兰大道\",\"gps_y\":\"113.913227\",\"gps_x\":\"35.282576\",\"addtime\":\"0\",\"addip\":\"\",\"paixu\":\"0\",\"type_nid\":\"xyd\",\"distance\":\"648\"},{\"agent_id\":\"3\",\"user_id\":\"6112\",\"agent_name\":\"新易贷技术部\",\"type\":\"0\",\"status\":\"1\",\"linkman\":\"技术部\",\"tel\":\"\",\"mobile\":\"\",\"description\":\"新易贷技术部\",\"address\":\"华兰大道和牧野大道交汇处\",\"gps_y\":\"113.916964\",\"gps_x\":\"35.286466\",\"addtime\":\"1439956117\",\"addip\":\"222.89.89.158\",\"paixu\":\"0\",\"type_nid\":\"xyd\",\"distance\":\"117\"},{\"agent_id\":\"4\",\"user_id\":\"6608\",\"agent_name\":\"新易贷车商\",\"type\":\"2\",\"status\":\"1\",\"linkman\":\"小王\",\"tel\":\"15600000000\",\"mobile\":\"15600000000\",\"description\":\"这是一个测试中的车商\",\"address\":\"华兰大道和牧野路交叉口\",\"gps_y\":\"113.91718\",\"gps_x\":\"35.287173\",\"addtime\":\"1439968969\",\"addip\":\"222.89.89.158\",\"paixu\":\"0\",\"type_nid\":\"\",\"distance\":\"42\"},{\"agent_id\":\"8\",\"user_id\":\"9206\",\"agent_name\":\"华盛房商\",\"type\":\"1\",\"status\":\"0\",\"linkman\":\"ceshi3\",\"tel\":\"15600000000\",\"mobile\":\"15600000000\",\"description\":\"测试\",\"address\":\"测试\",\"gps_y\":\"113.911502\",\"gps_x\":\"35.283991\",\"addtime\":\"1440053874\",\"addip\":\"222.89.89.158\",\"paixu\":\"0\",\"type_nid\":\"\",\"distance\":\"635\"},{\"agent_id\":\"22\",\"user_id\":\"9250\",\"agent_name\":\"南环房产信息中介\",\"type\":\"1\",\"status\":\"1\",\"linkman\":\"admin1\",\"tel\":\"15600062352\",\"mobile\":\"15600062352\",\"description\":\"\",\"address\":\"\",\"gps_y\":\"113.917413\",\"gps_x\":\"35.287401\",\"addtime\":\"1440645918\",\"addip\":\"222.89.89.158\",\"paixu\":\"0\",\"type_nid\":\"\",\"distance\":\"39\"},{\"agent_id\":\"30\",\"user_id\":\"9287\",\"agent_name\":\"金东旭房产\",\"type\":\"1\",\"status\":\"1\",\"linkman\":\"金东旭房产\",\"tel\":\"18738589173\",\"mobile\":\"18738589173\",\"description\":\"主要从事提供房屋出租，二手房买卖等房产信息。\",\"address\":\"牧野路东旭小区门口\",\"gps_y\":\"113.918545\",\"gps_x\":\"35.29958\",\"addtime\":\"1441176482\",\"addip\":\"125.42.146.102\",\"paixu\":\"0\",\"type_nid\":\"\",\"distance\":\"1347\"},{\"agent_id\":\"31\",\"user_id\":\"9288\",\"agent_name\":\"小会房产信息部\",\"type\":\"1\",\"status\":\"1\",\"linkman\":\"小会房产信息部\",\"tel\":\"15516540883\",\"mobile\":\"15516540883\",\"description\":\"房屋租赁二手房买卖二手房按揭贷款房屋评估等房产信息服务。\",\"address\":\"新飞大道与金穗大道十字北100米\",\"gps_y\":\"113.906993\",\"gps_x\":\"35.29958\",\"addtime\":\"1441178179\",\"addip\":\"125.42.146.102\",\"paixu\":\"0\",\"type_nid\":\"\",\"distance\":\"1619\"},{\"agent_id\":\"32\",\"user_id\":\"9289\",\"agent_name\":\"广森汽车服务\",\"type\":\"4\",\"status\":\"0\",\"linkman\":\"广森汽车服务\",\"tel\":\"0373-5021181\",\"mobile\":\"18803732077\",\"description\":\"主要从事汽车美容洗车售车饰等相关汽车服务\",\"address\":\"和平路南环十字北300米\",\"gps_y\":\"113.89571\",\"gps_x\":\"35.267916\",\"addtime\":\"1441178710\",\"addip\":\"125.42.146.102\",\"paixu\":\"0\",\"type_nid\":\"\",\"distance\":\"2912\"},{\"agent_id\":\"33\",\"user_id\":\"9290\",\"agent_name\":\"海洋不动产信息部\",\"type\":\"1\",\"status\":\"1\",\"linkman\":\"海洋不动产信息部\",\"tel\":\"18837336668\",\"mobile\":\"18837336668\",\"description\":\"主要做房屋租赁二手房买卖交易房屋抵押贷款。\",\"address\":\"平原路牧野路东500米\",\"gps_y\":\"113.922444\",\"gps_x\":\"35.310652\",\"addtime\":\"1441179444\",\"addip\":\"125.42.146.102\",\"paixu\":\"0\",\"type_nid\":\"\",\"distance\":\"2617\"},{\"agent_id\":\"34\",\"user_id\":\"9291\",\"agent_name\":\"协润汽车服务\",\"type\":\"5\",\"status\":\"1\",\"linkman\":\"协润汽车服务\",\"tel\":\"13072691155\",\"mobile\":\"13072691155\",\"description\":\"汽车维修美容洗车救援\",\"address\":\"牧野路人民路口东300米\",\"gps_y\":\"113.919632\",\"gps_x\":\"35.306278\",\"addtime\":\"1441180123\",\"addip\":\"125.42.146.102\",\"paixu\":\"0\",\"type_nid\":\"\",\"distance\":\"2098\"},{\"agent_id\":\"35\",\"user_id\":\"9292\",\"agent_name\":\"我爱我家房产信息部\",\"type\":\"1\",\"status\":\"1\",\"linkman\":\"我爱我家房产信息部\",\"tel\":\"13069361678\",\"mobile\":\"13069361678\",\"description\":\"房屋租赁房屋买卖过户\",\"address\":\"向阳路575号\",\"gps_y\":\"113.897614\",\"gps_x\":\"35.294052\",\"addtime\":\"1441180350\",\"addip\":\"125.42.146.102\",\"paixu\":\"0\",\"type_nid\":\"\",\"distance\":\"1903\"},{\"agent_id\":\"36\",\"user_id\":\"9293\",\"agent_name\":\"金基汽车租赁公司\",\"type\":\"2\",\"status\":\"1\",\"linkman\":\"金基汽车租赁公司\",\"tel\":\"15649602288\",\"mobile\":\"15649602288\",\"description\":\"汽车租赁洗车销售汽车配件\",\"address\":\"人民路牧野路南50米\",\"gps_y\":\"113.918599\",\"gps_x\":\"35.305946\",\"addtime\":\"1441180602\",\"addip\":\"125.42.146.102\",\"paixu\":\"0\",\"type_nid\":\"\",\"distance\":\"2052\"}]}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            RequestResult<List<BusinessInfo>> loginResult = mapper.readValue(replaceSpecialtyStr(json,null,null), new TypeReference<RequestResult<List<BusinessInfo>>>() {});
            System.out.println(loginResult.getMsg().getResultCode()+":"+loginResult.getMsg().getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static boolean isBlankOrNull(String str){
        if(null==str)return true;
        //return str.length()==0?true:false;
        return str.length()==0;
    }
    public static String replaceSpecialtyStr(String str,String pattern,String replace){
        if(isBlankOrNull(pattern))
            pattern="\\s*|\t|\r|\n";//去除字符串中空格、换行、制表
        if(isBlankOrNull(replace))
            replace="";
        return Pattern.compile(pattern).matcher(str).replaceAll(replace);

    }
}
