package collect.xyd.net.cn.xinyidaicollect.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.net.xyd.http.RequestUri;
import cn.net.xyd.view.AreaPopupWindow;
import cn.net.xyd.view.CustomDialog;
import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.entity.HouseInfoV1;
import collect.xyd.net.cn.xinyidaicollect.entity.Photo;
import collect.xyd.net.cn.xinyidaicollect.utils.FileNameUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.PictureUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/7/17 0017.
 */
public class HouseInfoFragment extends CollectInfoFragment implements AreaPopupWindow.OnAreaSelectListener {
    @Bind(R.id.et_area)
    EditText etArea;
    @Bind(R.id.et_house_title)
    EditText etHouseTitle;
    @Bind(R.id.rb_rent)
    RadioButton rbRent;
    @Bind(R.id.rb_second_hand_house)
    RadioButton rbSecondHandHouse;
    @Bind(R.id.rg_info_type)
    RadioGroup rgInfoType;
    @Bind(R.id.et_month_rent)
    EditText etMonthRent;
    @Bind(R.id.et_house_price)
    EditText etHousePrice;
    @Bind(R.id.et_house_acreage)
    EditText etHouseAcreage;
    @Bind(R.id.et_first_payment)
    EditText etFirstPayment;
    @Bind(R.id.et_house_layout)
    EditText etHouseLayout;
    @Bind(R.id.rb_general_house)
    RadioButton rbGeneralHouse;
    @Bind(R.id.rb_villa)
    RadioButton rbVilla;
    @Bind(R.id.rb_bungalow)
    RadioButton rbBungalow;
    @Bind(R.id.rg_house_type)
    RadioGroup rgHouseType;
    @Bind(R.id.et_house_floor)
    EditText etHouseFloor;
    @Bind(R.id.et_orientation)
    EditText etOrientation;
    @Bind(R.id.rb_general)
    RadioButton rbGeneral;
    @Bind(R.id.rb_fine)
    RadioButton rbFine;
    @Bind(R.id.rb_luxury)
    RadioButton rbLuxury;
    @Bind(R.id.rb_rough)
    RadioButton rbRough;
    @Bind(R.id.rg_decoration)
    RadioGroup rgDecoration;
    @Bind(R.id.et_build_time)
    EditText etBuildTime;
    @Bind(R.id.et_property)
    EditText etProperty;
    @Bind(R.id.et_contacts)
    EditText etContacts;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_house_address)
    EditText etHouseAddress;
    @Bind(R.id.btn_add_photo)
    Button btnAddPhoto;
    @Bind(R.id.et_house_description)
    EditText etHouseDescription;
    @Bind(R.id.et_neighborhood_description)
    EditText etNeighborhoodDescription;
    @Bind(R.id.et_traffic_description)
    EditText etTrafficDescription;
    @Bind(R.id.btn_publish)
    Button btnPublish;
    @Bind(R.id.ll_photo)
    LinearLayout llPhoto;
    @Bind(R.id.tv_month_rent)
    TextView tvMonthRent;
    @Bind(R.id.tv_house_price)
    TextView tvHousePrice;
    @Bind(R.id.tv_first_payment)
    TextView tvFirstPayment;
    @Bind(R.id.tv_build_time)
    TextView tvBuildTime;
    @Bind(R.id.tv_property)
    TextView tvProperty;
    private int infoType;//信息分类 1是租房，2是二手房
    private String houseType;//房屋类型
    private String decoration;//装修程度
    public static final int REQUEST_CODE_CAPTURE_CAMEIA = 1;//拍照
    public static final int REQUEST_CODE_PICK_IMAGE = 2;//从相册选择
    public static final String ARGUMENT = "argument";

    /**
     * 传入需要的参数，设置给arguments
     *
     * @param argument
     * @return
     */
    public static HouseInfoFragment newInstance(Serializable argument, int operate_type) {
        operateType = operate_type;
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGUMENT, argument);
        HouseInfoFragment contentFragment = new HouseInfoFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    private static int operateType = 1;//1是添加，2是修改
    private HouseInfoV1 houseInfoV1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            houseInfoV1 = (HouseInfoV1) bundle.getSerializable(ARGUMENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.house_info_layout, container, false);
        ButterKnife.bind(this, view);
        initData();
        initListener();
        return view;
    }

    private void initData() {
        infoType = Integer.valueOf(String.valueOf(rbRent.getTag()));
        houseType = rbGeneralHouse.getText().toString();
        decoration = rbGeneral.getText().toString();
    }

    private void initListener() {
        rgInfoType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_rent) {
                    tvMonthRent.setVisibility(View.VISIBLE);
                    etMonthRent.setVisibility(View.VISIBLE);

                    tvHousePrice.setVisibility(View.GONE);
                    etHousePrice.setVisibility(View.GONE);

                    tvFirstPayment.setVisibility(View.GONE);
                    etFirstPayment.setVisibility(View.GONE);

                    tvBuildTime.setVisibility(View.GONE);
                    etBuildTime.setVisibility(View.GONE);

                    tvProperty.setVisibility(View.GONE);
                    etProperty.setVisibility(View.GONE);
                    infoType = Integer.valueOf(String.valueOf(rbRent.getTag()));
                } else if (checkedId == R.id.rb_second_hand_house) {
                    tvHousePrice.setVisibility(View.VISIBLE);
                    etHousePrice.setVisibility(View.VISIBLE);

                    tvFirstPayment.setVisibility(View.VISIBLE);
                    etFirstPayment.setVisibility(View.VISIBLE);

                    tvBuildTime.setVisibility(View.VISIBLE);
                    etBuildTime.setVisibility(View.VISIBLE);

                    tvProperty.setVisibility(View.VISIBLE);
                    etProperty.setVisibility(View.VISIBLE);

                    tvMonthRent.setVisibility(View.GONE);
                    etMonthRent.setVisibility(View.GONE);
                    infoType = Integer.valueOf(String.valueOf(rbSecondHandHouse.getTag()));
                }
            }
        });
        rgHouseType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = null;
                switch (checkedId) {
                    case R.id.rb_general_house:
                        rb = rbGeneralHouse;
                        break;
                    case R.id.rb_villa:
                        rb = rbVilla;
                        break;
                    case R.id.rb_bungalow:
                        rb = rbBungalow;
                        break;
                }
                if (rb != null) {
                    houseType = rb.getText().toString();
                }
            }
        });
        rgDecoration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = null;
                switch (checkedId) {
                    case R.id.rb_general:
                        rb = rbGeneral;
                        break;
                    case R.id.rb_fine:
                        rb = rbFine;
                        break;
                    case R.id.rb_luxury:
                        rb = rbLuxury;
                        break;
                    case R.id.rb_rough:
                        rb = rbRough;
                        break;
                }
                if (rb != null) {
                    decoration = rb.getText().toString();
                }
            }
        });
        etArea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onClickArea(v);
                }
            }
        });
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getActivity(), v);
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.take_picture:
                                String state = Environment.getExternalStorageState();
                                if (state.equals(Environment.MEDIA_MOUNTED)) {
                                    File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "xydcollect" + File.separator);
                                    if (!dir.exists()) {
                                        dir.mkdirs();
                                    }
                                    File file = new File(dir, FileNameUtil.getInstance().getFileName("jpg"));
                                    photo = new Photo(file.getAbsolutePath(), null);
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                    startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
                                } else {
                                    T.showLong(getActivity(), "请确认已经插入SD卡");
                                }
                                break;
                            case R.id.select_picture:
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");//相片类型
                                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                                break;
                        }
                        return true;
                    }
                });
                pop.getMenuInflater().inflate(R.menu.popup_menu, pop.getMenu());
                pop.show();
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validIsEmpty()) {
                    T.showLong(getActivity(), validMsg);
                } else {
                    //文件信息
                    Map<String, File> fileMap = new HashMap<String, File>();
                    for (int i = 0; i < photos.size(); i++) {
                        File imageFile;
                        if ((imageFile = PictureUtil.writeImageFile2SDcard(getActivity(), photos.get(i).getPath())) != null) {
                            fileMap.put("image" + i, imageFile);
                        }
                    }
                    //文本信息
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("house_title", etHouseTitle.getText().toString());
                    params.put("house_info_type", infoType + "");
                    params.put("house_type", houseType);
                    params.put("house_decoration", decoration);
                    //params.put("house_area", etArea.getText().toString());
                    params.put("house_province", houseProvince);
                    params.put("house_city", houseCity);
                    params.put("house_district", houseDistrict);
                    switch (infoType) {
                        //租房
                        case 1:
                            params.put("house_month_rent", etMonthRent.getText().toString());
                            break;
                        //二手房
                        case 2:
                            params.put("house_price", etHousePrice.getText().toString());
                            params.put("house_first_payment", etFirstPayment.getText().toString());
                            params.put("house_build_time", etBuildTime.getText().toString());
                            params.put("house_property", etProperty.getText().toString());
                            break;
                    }
                    params.put("house_acreage", etHouseAcreage.getText().toString());
                    params.put("house_layout", etHouseLayout.getText().toString());
                    params.put("house_floor", etHouseFloor.getText().toString());
                    params.put("house_orientation", etOrientation.getText().toString());
                    params.put("house_contacts", etContacts.getText().toString());
                    params.put("house_phone", etPhone.getText().toString());
                    params.put("house_address", etHouseAddress.getText().toString());
                    params.put("house_description", etHouseDescription.getText().toString());
                    params.put("house_neighborhood_desc", etNeighborhoodDescription.getText().toString());
                    params.put("house_traffic_desc", etTrafficDescription.getText().toString());

                    commitListener.commit(RequestUri.COMMIT_HOUSE_INFO, params, fileMap);
                }


            }
        });
    }

    //表单验证信息
    private String validMsg;

    //验证表单
    private boolean validIsEmpty() {
        if (TextUtils.isEmpty(etHouseTitle.getText())) {
            validMsg = "标题不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etArea.getText())) {
            validMsg = "地区不能为空";
            return false;
        }
        switch (rgInfoType.getCheckedRadioButtonId()) {
            case R.id.rb_rent:
                if (TextUtils.isEmpty(etMonthRent.getText())) {
                    validMsg = "月租不能为空";
                    return false;
                }
                break;
            case R.id.rb_second_hand_house:
                if (TextUtils.isEmpty(etHousePrice.getText())) {
                    validMsg = "房价不能为空";
                    return false;
                }
                if (TextUtils.isEmpty(etFirstPayment.getText())) {
                    validMsg = "首付不能为空";
                    return false;
                }
                if (TextUtils.isEmpty(etBuildTime.getText())) {
                    validMsg = "建造时间不能为空";
                    return false;
                }
                if (TextUtils.isEmpty(etProperty.getText())) {
                    validMsg = "产权不能为空";
                    return false;
                }
                break;
        }
        if (TextUtils.isEmpty(etHouseAcreage.getText())) {
            validMsg = "面积不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etHouseLayout.getText())) {
            validMsg = "户型不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etHouseFloor.getText())) {
            validMsg = "楼层不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etOrientation.getText())) {
            validMsg = "朝向不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etContacts.getText())) {
            validMsg = "联系人不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etPhone.getText())) {
            validMsg = "电话不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etHouseAddress.getText())) {
            validMsg = "地址不能为空";
            return false;
        }
        if (photos.size() <= 0) {
            validMsg = "请添加照片";
            return false;
        }
        if (TextUtils.isEmpty(etHouseDescription.getText())) {
            validMsg = "房源描述不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etNeighborhoodDescription.getText())) {
            validMsg = "周边情况介绍不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etTrafficDescription.getText())) {
            validMsg = "交通状况描述不能为空";
            return false;
        }
        return true;
    }

    private Photo photo;
    private List<Photo> photos = new ArrayList<Photo>();
    private List<ImageView> imageViews = new ArrayList<ImageView>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                if (data != null) {
                    Uri uri = data.getData();
                    String img_path = null;
                    Cursor cursor = null;
                    String[] proj = {MediaStore.Images.Media.DATA};
                    if (Build.VERSION.SDK_INT >= 17) {
                        cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
                    } else {
                        cursor = getActivity().managedQuery(uri, proj, null, null, null);
                    }
                    if (cursor != null && cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        img_path = cursor.getString(column_index);
                        cursor.close();
                        Photo photoTemp = new Photo(img_path, null);
                        photos.add(photoTemp);
                        ImageView imageView = addOneImageView();
                        imageView.setImageBitmap(getPreview(img_path));
                        imageViews.add(imageView);

                        if (imageViews.size() == 4) {
                            btnAddPhoto.setVisibility(View.GONE);
                        }
                    } else {
                        T.showLong(getActivity(), "获取不到图片");
                    }

                }
            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
                /*FileInputStream in = null;
                try {
                    in = new FileInputStream(photo.getPath());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 10;
                    Bitmap bmp = BitmapFactory.decodeStream(in, null, options);

                    photo.setBitmap(bmp);
                    photos.add(photo);

                    ImageView imageView = addOneImageView();
                    imageView.setImageBitmap(bmp);

                    imageViews.add(imageView);

                    if (imageViews.size() == 4) {
                        btnAddPhoto.setVisibility(View.GONE);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }*/
                Bitmap bitmap = getPreview(photo.getPath());

                photo.setBitmap(bitmap);
                photos.add(photo);

                ImageView imageView = addOneImageView();
                imageView.setImageBitmap(bitmap);

                imageViews.add(imageView);

                if (imageViews.size() == 4) {
                    btnAddPhoto.setVisibility(View.GONE);
                }

            }
        }
    }

    private int id = 0;

    //新增一个imageView
    private ImageView addOneImageView() {
        ImageView imageView = new ImageView(getActivity());
        imageView.setId(++id);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(117, 117));
        imageView.setPadding(0, 0, 10, 0);
        llPhoto.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImg(readBitmapFromPath(photos.get(v.getId() - 1).getPath()));
            }
        });
        return imageView;
    }

    //从给定路径中读取图片
    private Bitmap readBitmapFromPath(String path) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
            return bmp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @OnClick(R.id.et_area)
    public void onClickArea(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        new AreaPopupWindow(this).showArea(view);
    }

    private String houseProvince;
    private String houseCity;
    private String houseDistrict;

    @Override
    public void onSelect(String provinceName, String cityName, String districtName, String areaCode) {
        /*T.showLong(getActivity(), "当前选中:" + provinceName + "," + cityName + ","
                + districtName + "," + areaCode);*/
        houseProvince = provinceName;
        houseCity = cityName;
        houseDistrict = districtName;
        etArea.setText(provinceName + cityName + districtName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void showImg(Bitmap bitmap) {
        if (bitmap != null) {
            // 初始化一个自定义的Dialog
            final CustomDialog.Builder b = new CustomDialog.Builder(getActivity());
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setImageBitmap(bitmap);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b.dismiss();
                }
            });
            b.setView(imageView);
            b.show();
        } else {
            T.showLong(getActivity(), "无法打开大图，文件可能已损坏");
        }
    }

}
