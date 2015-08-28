package collect.xyd.net.cn.xinyidaicollect.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.net.xyd.http.RequestUri;
import cn.net.xyd.view.FlowRadioGroup;
import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.utils.PictureUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/8/14 0014.
 */
public class ShopBusinessFragment extends CollectInfoFragment {
    @Bind(R.id.et_shop_title)
    EditText etShopTitle;
    @Bind(R.id.et_shop_tel)
    EditText etShopTel;
    @Bind(R.id.rb_car_wash)
    RadioButton rbCarWash;
    @Bind(R.id.rb_car_garage)
    RadioButton rbCarGarage;
    @Bind(R.id.rg_shop_type)
    FlowRadioGroup rgShopType;
    @Bind(R.id.et_shop_contacts)
    EditText etShopContacts;
    @Bind(R.id.et_shop_mobile_phone)
    EditText etShopMobilePhone;
    @Bind(R.id.et_shop_address)
    EditText etShopAddress;
    @Bind(R.id.et_shop_description)
    EditText etShopDescription;
    @Bind(R.id.btn_commit)
    Button btnCommit;
    private View view;
    private int shopType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.shop_business_layout, container, false);
        } else {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        ButterKnife.bind(this, view);
        initData();
        initListener();
        return view;
    }

    private void initData() {
        shopType = Integer.valueOf(String.valueOf(rbCarWash.getTag()));
    }

    private void initListener() {
        rgShopType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_car_wash) {
                    shopType = Integer.valueOf(String.valueOf(rbCarWash.getTag()));
                } else if (checkedId == R.id.rb_car_garage) {
                    shopType = Integer.valueOf(String.valueOf(rbCarGarage.getTag()));
                }
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validIsEmpty()) {
                    T.showLong(getActivity(), validMsg);
                } else {
                    //文本信息
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("agent_name", etShopTitle.getText().toString());
                    params.put("type", shopType + "");
                    params.put("linkman", etShopContacts.getText().toString());
                    params.put("tel", etShopTel.getText().toString());
                    params.put("mobile", etShopMobilePhone.getText().toString());
                    params.put("description", etShopDescription.getText().toString());
                    params.put("address", etShopAddress.getText().toString());
                    commitListener.commit(RequestUri.COMMIT_SHOP_INFO, params, null);
                }
            }
        });
    }
    //表单验证信息
    private String validMsg;
    //验证表单
    private boolean validIsEmpty() {
        if (TextUtils.isEmpty(etShopTitle.getText())) {
            validMsg = "标题不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etShopContacts.getText())) {
            validMsg = "联系人不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etShopTel.getText())) {
            validMsg = "电话不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etShopMobilePhone.getText())) {
            validMsg = "手机不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etShopDescription.getText())) {
            validMsg = "店铺描述不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etShopAddress.getText())) {
            validMsg = "地址不能为空";
            return false;
        }
        return true;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
