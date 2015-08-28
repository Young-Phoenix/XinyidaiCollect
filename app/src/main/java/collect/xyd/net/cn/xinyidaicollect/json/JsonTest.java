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

        String json = "{\"msg\": {\"resultcode\": \"200\",\"message\": \"列表获取成功\"},\"data\":[{\"agent_id\":\"1\",\"user_id\":\"6709\",\"agent_name\":\"北京新易贷投资顾问有限公司\",\"type\":\"2\",\"status\":\"1\",\"linkman\":\"周吉宏\",\"tel\":\"400-886-7860\",\"mobile\":\"18790696575\",\"description\":\" 新易贷拥有雄厚的资金背景，先进的管理体系，专业的运营团队，一流的用户体验，主要服务于借贷双方的个人及中小微企业，通过提供优质的平台服务达成投资方、借贷方共同受益的双赢局面。公司以“服务他人，成就自己”为发展理念，希望经过我们的专业程度和不懈努力，帮助更多的人和企业，完成自己的梦想。\",\"address\":\"河南省新乡市华兰大道509号创业园三号园南楼一楼西厅\",\"gps_y\":\"113.919982\",\"gps_x\":\"35.291828\",\"addtime\":\"1436408619\",\"addip\":\"127.0.0.1\",\"paixu\":\"0\",\"distance\":\"550\"},{\"agent_id\":\"7\",\"user_id\":\"6613\",\"agent_name\":\"新易贷房产中介测试一\",\"type\":\"1\",\"status\":\"1\",\"linkman\":\"周\",\"tel\":\"400-886-7860\",\"mobile\":\"18790696575\",\"description\":\"新易贷房产中介描述内容测试一\",\"address\":\"牧野路\",\"gps_y\":\"113.918478\",\"gps_x\":\"35.289088\",\"addtime\":\"1437702574\",\"addip\":\"222.89.89.158\",\"paixu\":\"0\",\"distance\":\"219\"},{\"agent_id\":\"18\",\"user_id\":\"6691\",\"agent_name\":\"郑州车商测试\",\"type\":\"2\",\"status\":\"1\",\"linkman\":\"沃德\",\"tel\":\"400-886-7860\",\"mobile\":\"13222913977\",\"description\":\"沃德资产二手车商\",\"address\":\"郑州市中州大道与三全路交叉口向西100米路南圣力德二手名车\",\"gps_y\":\"113.918725\",\"gps_x\":\"35.288381\",\"addtime\":\"1438569834\",\"addip\":\"222.89.89.158\",\"paixu\":\"0\",\"distance\":\"180\"},{\"agent_id\":\"30\",\"user_id\":\"6693\",\"agent_name\":\"洗车店测试一\",\"type\":\"4\",\"status\":\"1\",\"linkman\":\"洗车\",\"tel\":\"400-886-7860\",\"mobile\":\"13222913977\",\"description\":\"专业洗车一百年。。。\",\"address\":\"牧野路与华兰路交叉口\",\"gps_y\":\"113.920755\",\"gps_x\":\"35.287585\",\"addtime\":\"1439537541\",\"addip\":\"222.89.89.158\",\"paixu\":\"0\",\"distance\":\"335\"},{\"agent_id\":\"31\",\"user_id\":\"0\",\"agent_name\":\"测试洗车行\",\"type\":\"4\",\"status\":\"0\",\"linkman\":\"测试\",\"tel\":\"123456\",\"mobile\":\"123456789\n" +
                "08-18 09:49:48.173    2946-2946/collect.xyd.net.cn.xinyidaicollect W/System.err﹕ \",\"description\":\"洗车，装潢\",\"address\":\"巫婆理发\",\"gps_y\":\"113.91703953273175\",\"gps_x\":\"35.287500785443875\",\"addtime\":\"0\",\"addip\":\"\",\"paixu\":\"0\",\"distance\":\"3\"},{\"agent_id\":\"32\",\"user_id\":\"5224\",\"agent_name\":\"ceshi aaa\",\"type\":\"4\",\"status\":\"1\",\"linkman\":\"xiaoai\",\"tel\":\"15600000000\",\"mobile\":\"15600000000\",\"description\":\"sadfasdfas\",\"address\":\"asdfasdfasdf\",\"gps_y\":\"113.917395\",\"gps_x\":\"35.284109\",\"addtime\":\"1439797587\",\"addip\":\"222.89.89.158\",\"paixu\":\"0\",\"distance\":\"377\"}]}";
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
