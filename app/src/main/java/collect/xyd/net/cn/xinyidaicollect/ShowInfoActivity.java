package collect.xyd.net.cn.xinyidaicollect;

import android.os.Bundle;

import java.io.Serializable;

import collect.xyd.net.cn.xinyidaicollect.entity.CarInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.HouseInfoV1;
import collect.xyd.net.cn.xinyidaicollect.entity.HouseInfoV2;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;

/**
 * Created by Administrator on 2015/8/7 0007.
 */
public class ShowInfoActivity extends ModifyActivity {
    private int info_type;//0未审核；1未通过；2已审核
    private CarInfo carInfo = null;
    private HouseInfoV2 houseInfo = null;
    private Serializable info;
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        info_type = bundle.getInt(Constants.INFO_TYPE);
        type = bundle.getInt(Constants.TYPE);
        info = bundle.getSerializable(Constants.INFO);
        switch (type) {
            case 1:
                houseInfo = (HouseInfoV2) info;
                break;
            case 2:
                carInfo = (CarInfo) info;
                break;
        }
        if(savedInstanceState==null){
            savedInstanceState = new Bundle();
        }
        savedInstanceState.putBundle("bundle_key",bundle);
        super.onCreate(savedInstanceState);
    }
}
