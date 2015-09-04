package collect.xyd.net.cn.xinyidaicollect.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import collect.xyd.net.cn.xinyidaicollect.entity.Photo;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.SPUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/8/14 0014.
 */
public class ShopBusinessFragment extends VideoCollectInfoFragment implements View.OnClickListener {
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
    @Bind(R.id.rb_fang)
    RadioButton rbFang;
    @Bind(R.id.rb_che)
    RadioButton rbChe;
    @Bind(R.id.iv_inner_video_image)
    ImageView ivInnerVideoImage;
    @Bind(R.id.btn_add_inner_video)
    Button btnAddInnerVideo;
    @Bind(R.id.iv_outer_video_image)
    ImageView ivOuterVideoImage;
    @Bind(R.id.btn_add_outer_video)
    Button btnAddOuterVideo;
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
        llPhoto = (LinearLayout) view.findViewById(R.id.ll_photo);
        btnAddPhoto = (Button) view.findViewById(R.id.btn_add_photo);
        initData();
        initListener();
        return view;
    }

    private void initData() {
        shopType = Integer.valueOf(String.valueOf(rbFang.getTag()));
        etShopAddress.setText(SPUtils.get(getActivity(), Constants.KEY_ADDRESS, "").toString());
    }

    private void initListener() {
        rgShopType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_car_wash) {
                    shopType = Integer.valueOf(String.valueOf(rbCarWash.getTag()));
                } else if (checkedId == R.id.rb_car_garage) {
                    shopType = Integer.valueOf(String.valueOf(rbCarGarage.getTag()));
                } else if (checkedId == R.id.rb_che) {
                    shopType = Integer.valueOf(String.valueOf(rbChe.getTag()));
                } else if (checkedId == R.id.rb_fang) {
                    shopType = Integer.valueOf(String.valueOf(rbFang.getTag()));
                }
            }
        });

        btnAddPhoto.setOnClickListener(this);
        btnAddInnerVideo.setOnClickListener(this);
        btnAddOuterVideo.setOnClickListener(this);
        ivInnerVideoImage.setOnLongClickListener(new VideoImageLongClickListener(DELETE_INNER, 1, 0, ivInnerVideoImage, btnAddInnerVideo,null));
        ivOuterVideoImage.setOnLongClickListener(new VideoImageLongClickListener(DELETE_OUTER, 1, 0, ivOuterVideoImage, btnAddOuterVideo,null));
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validIsEmpty()) {
                    T.showLong(getActivity(), validMsg);
                } else {
                    //文件信息
                    Map<String, File> fileMap = new HashMap<String, File>();
                    for (int i = 0; i < photos.size(); i++) {
                        File imageFile;
                        if ((imageFile=photos.get(i).getFile())!=null) {
                            fileMap.put("image" + i, imageFile);
                        }
                    }
                    fileMap.put("video_inside", new File(innerVideoPath));
                    fileMap.put("video_outside", new File(outerVideoPath));
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
        if (photos.size() <= 0) {
            validMsg = "请添加照片";
            return false;
        }
        if(TextUtils.isEmpty(innerVideoPath)||TextUtils.isEmpty(outerVideoPath)){
            validMsg = "请添加视频";
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_photo:
                photoListener(v);
                break;
            case R.id.btn_add_inner_video:
                videoListener(v,REQUEST_CODE_CAPTURE_INNER_VIDEO,REQUEST_CODE_PICK_INNER_VIDEO);
                break;
            case R.id.btn_add_outer_video:
                videoListener(v,REQUEST_CODE_CAPTURE_OUTER_VIDEO,REQUEST_CODE_PICK_OUTER_VIDEO);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                processPickPhoto(data);
            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
                processCapturePhoto();
            } else if (requestCode == REQUEST_CODE_PICK_INNER_VIDEO || requestCode == REQUEST_CODE_CAPTURE_INNER_VIDEO) {
                processVideo(data, requestCode, ivInnerVideoImage, btnAddInnerVideo);
            } else if (requestCode == REQUEST_CODE_PICK_OUTER_VIDEO || requestCode == REQUEST_CODE_CAPTURE_OUTER_VIDEO) {
                processVideo(data, requestCode, ivOuterVideoImage, btnAddOuterVideo);
            }
        }
    }
}
