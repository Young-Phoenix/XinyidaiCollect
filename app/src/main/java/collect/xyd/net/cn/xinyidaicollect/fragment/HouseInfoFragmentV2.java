package collect.xyd.net.cn.xinyidaicollect.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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

import com.dd.CircularProgressButton;
import com.golshadi.majid.core.DownloadManagerPro;
import com.golshadi.majid.report.listener.DownloadManagerListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.net.xyd.http.HttpRequestUtil;
import cn.net.xyd.http.RequestUri;
import cn.net.xyd.view.AreaPopupWindow;
import cn.net.xyd.view.CustomDialog;
import collect.xyd.net.cn.xinyidaicollect.App;
import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.entity.HouseImageInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.HouseInfoV2;
import collect.xyd.net.cn.xinyidaicollect.entity.HouseVideoInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.Photo;
import collect.xyd.net.cn.xinyidaicollect.entity.VideoInfo;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.FileNameUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.L;
import collect.xyd.net.cn.xinyidaicollect.utils.PictureUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.SPUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * Created by Administrator on 2015/7/17 0017.
 */
public class HouseInfoFragmentV2 extends CollectInfoFragment implements AreaPopupWindow.OnAreaSelectListener, View.OnClickListener{
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
    @Bind(R.id.iv_inner_video_image)
    ImageView ivInnerVideoImage;
    @Bind(R.id.circularButton_inner_video)
    CircularProgressButton circularButtonInnerVideo;
    @Bind(R.id.btn_add_inner_video)
    Button btnAddInnerVideo;
    @Bind(R.id.iv_outer_video_image)
    ImageView ivOuterVideoImage;
    @Bind(R.id.circularButton_outer_video)
    CircularProgressButton circularButtonOuterVideo;
    @Bind(R.id.btn_add_outer_video)
    Button btnAddOuterVideo;
    private int infoType;//信息分类 1是租房，2是二手房
    private String houseType;//房屋类型
    private String decoration;//装修程度
    public static final String ARGUMENT = "argument";

    /**
     * 传入需要的参数，设置给arguments
     *
     * @param argument
     * @return
     */
    public static HouseInfoFragmentV2 newInstance(Serializable argument, int operate_type) {
        operateType = operate_type;
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGUMENT, argument);
        HouseInfoFragmentV2 contentFragment = new HouseInfoFragmentV2();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    private static int operateType = 1;//1是添加，2是修改
    private HouseInfoV2 houseInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            houseInfo = (HouseInfoV2) bundle.getSerializable(ARGUMENT);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.house_info_layout_v2, container, false);
        ButterKnife.bind(this, view);
        initData();
        initListener();
        return view;
    }

    private DisplayImageOptions options;

    private void initData() {
        infoType = Integer.valueOf(String.valueOf(rbRent.getTag()));
        houseType = rbGeneralHouse.getText().toString();
        decoration = rbGeneral.getText().toString();
        if (operateType == 2 && getArguments() != null) {
            this.options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.qian_default_image) // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.qian_default_image) // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.qian_default_image) // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                    .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                    .build(); // 创建配置过得DisplayImageOption对象
            houseInfo = (HouseInfoV2) getArguments().getSerializable(ARGUMENT);
            etHouseTitle.setText(houseInfo.getTitle());
            etArea.setText(houseInfo.getProvince() + houseInfo.getCity() + houseInfo.getArea());
            etMonthRent.setText(houseInfo.getShoujia() + "");
            etHousePrice.setText(houseInfo.getShoujia() + "");
            etHouseAcreage.setText(houseInfo.getMianji() + "");
            etFirstPayment.setText(houseInfo.getFangdai() + "");
            etHouseLayout.setText(houseInfo.getHuxing());
            etHouseFloor.setText(houseInfo.getLeixing());
            etOrientation.setText(houseInfo.getChaox());
            etBuildTime.setText(houseInfo.getNiandai());
            etProperty.setText(houseInfo.getChanquan()+"");
            etContacts.setText(houseInfo.getLianxiren());
            etPhone.setText(houseInfo.getTel());
            etHouseAddress.setText(houseInfo.getAddress());
            etHouseDescription.setText(houseInfo.getFangyuan_descirbe());
            etNeighborhoodDescription.setText(houseInfo.getXiaoqu_describe());
            etTrafficDescription.setText(houseInfo.getJiaotong());
            btnPublish.setText("提交");
            //租房or二手房
            if (houseInfo.getCate() == 1) {
                rbRent.setChecked(true);
            } else if (houseInfo.getCate() == 2) {
                rbSecondHandHouse.setChecked(true);
                infoType = Integer.valueOf(String.valueOf(rbSecondHandHouse.getTag()));
            }
            switchRentOrBuy(houseInfo.getCate());
            //房屋类型
            if (houseInfo.getGaikuang() != null && houseInfo.getGaikuang().contains("别墅")) {
                rbVilla.setChecked(true);
            } else if (houseInfo.getGaikuang() != null && houseInfo.getGaikuang().contains("平房")) {
                rbBungalow.setChecked(true);
            } else {
                rbGeneralHouse.setChecked(true);
            }
            switchHouseType(rgHouseType.getCheckedRadioButtonId());

            //装修类型
            if (houseInfo.getZhuangxiu() != null && houseInfo.getZhuangxiu().contains("精装修")) {
                rbFine.setChecked(true);
            } else if (houseInfo.getZhuangxiu() != null && houseInfo.getZhuangxiu().contains("普通装修")) {
                rbGeneral.setChecked(true);
            } else if (houseInfo.getZhuangxiu() != null && houseInfo.getZhuangxiu().contains("豪华装修")) {
                rbLuxury.setChecked(true);
            } else {
                rbRough.setChecked(true);
            }
            switchDecoration(rgDecoration.getCheckedRadioButtonId());

            //图片
            if (houseInfo.getImages() != null) {
                for (int i = 0; i < houseInfo.getImages().size(); i++) {
                    addImageView(llPhoto, houseInfo.getImages().get(i));
                }
            }
            if(houseInfo.getImages().size()>=4){
                btnAddPhoto.setVisibility(View.GONE);
            }

            HouseVideoInfo video_inside;
            if (houseInfo.getVideo() != null && (video_inside = houseInfo.getVideo().get("video_inside")) != null && !TextUtils.isEmpty(video_inside.getVideo_path())) {
                circularButtonInnerVideo.setVisibility(View.VISIBLE);
                ivInnerVideoImage.setTag(video_inside);
                btnAddInnerVideo.setVisibility(View.GONE);
                final String httpVideoPath = video_inside.getVideo_path();
                String[] files = httpVideoPath.split("/");
                downInnerVideoFileName = files[files.length - 1];
                String fileName = downInnerVideoFileName.split("\\.")[0];
                circularButtonInnerVideo.setIndeterminateProgressMode(true);
                circularButtonInnerVideo.setOnClickListener(new DownloadClickListener(httpVideoPath, fileName, 0, 1));
            }
            HouseVideoInfo video_outside;
            if (houseInfo.getVideo() != null && (video_outside = houseInfo.getVideo().get("video_outside")) != null && !TextUtils.isEmpty(video_outside.getVideo_path())) {
                circularButtonOuterVideo.setVisibility(View.VISIBLE);
                ivOuterVideoImage.setTag(video_outside);
                btnAddOuterVideo.setVisibility(View.GONE);
                final String httpVideoPath = video_outside.getVideo_path();
                String[] files = httpVideoPath.split("/");
                downOuterVideoFileName = files[files.length - 1];
                String fileName = downOuterVideoFileName.split("\\.")[0];
                circularButtonOuterVideo.setIndeterminateProgressMode(true);
                circularButtonOuterVideo.setOnClickListener(new DownloadClickListener(httpVideoPath, fileName, 4, 5));
            }
        }else{
            houseProvince = SPUtils.get(getActivity(),Constants.KEY_PROVINCE,"").toString();
            houseCity = SPUtils.get(getActivity(),Constants.KEY_CITY,"").toString();
            houseDistrict = SPUtils.get(getActivity(),Constants.KEY_DISTRICT,"").toString();

            etHouseAddress.setText(SPUtils.get(getActivity(),Constants.KEY_ADDRESS,"").toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_inner_video:
                videoListener(v,REQUEST_CODE_CAPTURE_INNER_VIDEO, REQUEST_CODE_PICK_INNER_VIDEO);
                break;
            case R.id.btn_add_outer_video:
                videoListener(v, REQUEST_CODE_CAPTURE_OUTER_VIDEO, REQUEST_CODE_PICK_OUTER_VIDEO);
                break;
        }
    }

    private class DownloadClickListener implements View.OnClickListener {
        private String httpAudioPath;
        private String downFileName;
        private int beginWhat;
        private int endWhat;

        public DownloadClickListener(String httpAudioPath, String downFileName, int beginwhat, int endwhat) {
            this.httpAudioPath = httpAudioPath;
            this.downFileName = downFileName;
            this.beginWhat = beginwhat;
            this.endWhat = endwhat;
        }

        @Override
        public void onClick(final View v) {
            {
                DownloadManagerPro dm = new DownloadManagerPro(getActivity().getApplicationContext());
                final String filePath = getActivity().getApplication().getPackageName() + File.separator + "audio/";
                dm.init(filePath, 12, new DownloadManagerListener() {
                    @Override
                    public void OnDownloadStarted(long taskId) {
                    }

                    @Override
                    public void OnDownloadPaused(long taskId) {
                    }

                    @Override
                    public void onDownloadProcess(long taskId, double percent, long downloadedLength) {
                        L.d(percent + "");
                        Message msg = new Message();
                        msg.arg1 = (int) percent;
                        msg.what = beginWhat;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void OnDownloadFinished(long taskId) {

                    }

                    @Override
                    public void OnDownloadRebuildStart(long taskId) {

                    }

                    @Override
                    public void OnDownloadRebuildFinished(long taskId) {
                        Message msg = new Message();
                        msg.getData().putString("filepath", filePath);
                        msg.what = endWhat;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void OnDownloadCompleted(long taskId) {

                    }

                    @Override
                    public void connectionLost(long taskId) {

                    }
                });

                int taskToken = dm.addTask(downFileName, Constants.SERVER_HOUSE_IP + httpAudioPath, true, false);
                try {
                    dm.startDownload(taskToken);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String basePath = Environment.getExternalStorageDirectory() + File.separator + msg.getData().getString("filepath");
            switch (msg.what) {
                case 0:
                    circularButtonInnerVideo.setProgress(msg.arg1);
                    break;
                case 1:
                    innerVideoPath = basePath + downInnerVideoFileName;
                    //ThumbnailUtils类2.2以上可用
                    Bitmap bitmap1 = ThumbnailUtils.createVideoThumbnail(innerVideoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
                    ivInnerVideoImage.setImageBitmap(bitmap1);
                    ivInnerVideoImage.setVisibility(View.VISIBLE);
                    circularButtonInnerVideo.setVisibility(View.GONE);
                    break;
                case 4:
                    circularButtonOuterVideo.setProgress(msg.arg1);
                    break;
                case 5:
                    outerVideoPath = basePath + downOuterVideoFileName;
                    //ThumbnailUtils类2.2以上可用
                    Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(outerVideoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
                    ivOuterVideoImage.setImageBitmap(bitmap2);
                    ivOuterVideoImage.setVisibility(View.VISIBLE);
                    circularButtonOuterVideo.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private String downInnerVideoFileName;
    private String downOuterVideoFileName;
    private void switchRentOrBuy(int type) {
        switch (type) {
            case 1://租房
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
                break;
            case 2://二手房
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
                break;
        }
    }

    private void switchHouseType(int checkedId) {
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

    private void switchDecoration(int checkedId) {
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

    private void initListener() {
        rgInfoType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_rent) {
                    switchRentOrBuy(1);
                } else if (checkedId == R.id.rb_second_hand_house) {
                    switchRentOrBuy(2);
                }
            }
        });
        rgHouseType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchHouseType(checkedId);
            }
        });
        rgDecoration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchDecoration(checkedId);
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

        btnAddInnerVideo.setOnClickListener(this);
        btnAddOuterVideo.setOnClickListener(this);
        ivInnerVideoImage.setOnClickListener(new VideoImageClickListener(innerVideoPath));
        ivOuterVideoImage.setOnClickListener(new VideoImageClickListener(outerVideoPath));
        ivInnerVideoImage.setOnLongClickListener(new VideoImageLongClickListener(DELETE_INNER,operateType,DELETE_HOUSE_VIDEO,ivInnerVideoImage,btnAddInnerVideo,RequestUri.DELETE_HOUSE_FILE));
        ivOuterVideoImage.setOnLongClickListener(new VideoImageLongClickListener(DELETE_OUTER,operateType,DELETE_HOUSE_VIDEO,ivOuterVideoImage,btnAddOuterVideo,RequestUri.DELETE_HOUSE_FILE));
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
                                    File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "xinyidai/picture" + File.separator);
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
                        if ((imageFile=photos.get(i).getFile())!=null) {
                            fileMap.put("image" + i, imageFile);
                        }
                    }

                    if (!TextUtils.isEmpty(innerVideoPath)) {
                        fileMap.put("video_inside", new File(innerVideoPath));
                    }
                    if (!TextUtils.isEmpty(outerVideoPath)) {
                        fileMap.put("video_outside", new File(outerVideoPath));
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
                    if (operateType == 2) {
                        commitListener.commit(RequestUri.MODIFY_HOUSE_INFO, params, fileMap);
                    } else {
                        commitListener.commit(RequestUri.COMMIT_HOUSE_INFO, params, fileMap);
                    }
                }


            }
        });
    }
    private static final int DELETE_HOUSE_VIDEO = 4;
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
    private int images;
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
                        File file = new File(img_path);
                        if (file != null && file.exists()) {
                            File imageFile = PictureUtil.writeImageFile2SDcard(getActivity(), img_path);
                            if(imageFile.exists()){
                                L.e(imageFile.getAbsolutePath());
                            }else{
                                L.e("文件不存在");
                            }
                            Photo photoTemp = new Photo(img_path, null);
                            photoTemp.setFile(imageFile);
                            photos.add(photoTemp);
                            ImageView imageView = addOneImageView(photoTemp);
                            imageView.setImageBitmap(getPreview(img_path));
                            hiddenAddPhotoBtn();
                        }else{
                            T.showLong(getActivity(), "图片不存在");
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
                File imageFile = PictureUtil.writeImageFile2SDcard(getActivity(), photo.getPath());
                photo.setFile(imageFile);
                photos.add(photo);

                ImageView imageView = addOneImageView(photo);
                imageView.setImageBitmap(bitmap);
                hiddenAddPhotoBtn();

            } else if (requestCode == REQUEST_CODE_PICK_INNER_VIDEO || requestCode == REQUEST_CODE_CAPTURE_INNER_VIDEO) {
                processVideo(data, requestCode,ivInnerVideoImage,btnAddInnerVideo);
            } else if (requestCode == REQUEST_CODE_PICK_OUTER_VIDEO || requestCode == REQUEST_CODE_CAPTURE_OUTER_VIDEO) {
                processVideo(data, requestCode,ivOuterVideoImage,btnAddOuterVideo);
            }
        }
    }


    //新增一个imageView
    private ImageView addImageView(ViewGroup parent, final HouseImageInfo imageInfo) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setTag(imageInfo);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(117, 117));
        imageView.setPadding(0, 0, 10, 0);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确认删除吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        removeImageFromServer(imageInfo, Constants.DELETE_IMAGE);
                        houseInfo.getImages().remove(imageInfo);
                        v.setVisibility(View.GONE);
                        showAddPhotoBtn();
                    }

                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }

        });
        parent.addView(imageView);
        ImageLoader.getInstance().displayImage(Constants.SERVER_HOUSE_IP + imageInfo.getImg_path(),
                imageView, options,
                new AnimateFirstDisplayListener());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBigImg(imageInfo);
            }
        });
        hiddenAddPhotoBtn();
        return imageView;
    }

    private void hiddenAddPhotoBtn(){
        if(++images>=4){
            btnAddPhoto.setVisibility(View.GONE);
        }
    }

    private void showAddPhotoBtn(){
        if(--images<4 && btnAddPhoto.getVisibility() == View.GONE){
            btnAddPhoto.setVisibility(View.VISIBLE);
        }
    }

    private void showBigImg(HouseImageInfo imageInfo) {
        // 初始化一个自定义的Dialog
        final CustomDialog.Builder b = new CustomDialog.Builder(getActivity());
        ImageView imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        b.setView(imageView);
        b.show();
        ImageLoader.getInstance().displayImage(Constants.SERVER_HOUSE_IP + imageInfo.getImg_path(),
                imageView, options,
                new AnimateFirstDisplayListener());
    }

    private void removeImageFromServer(HouseImageInfo carImageInfo, int requestCode) {
        Map params = new HashMap();
        params.put(Constants.TOKEN, ((App) getActivity().getApplication()).user.getToken());
        //params.put("che_id", carImageInfo.getChe_id() + "");
        params.put("img_id", carImageInfo.getImg_id() + "");
        new HttpRequestUtil(this).stringRequest(requestCode, RequestUri.DELETE_HOUSE_FILE, params);
    }

    private int id = 0;

    //新增一个imageView
    private ImageView addOneImageView(Photo photo) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setTag(photo);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(117, 117));
        imageView.setPadding(0, 0, 10, 0);
        llPhoto.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImg(readBitmapFromPath(((Photo) v.getTag()).getPath()));
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.are_you_sure_delete);
                builder.setTitle(R.string.notice);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        photos.remove((Photo) v.getTag());
                        v.setVisibility(View.GONE);
                        showAddPhotoBtn();
                    }

                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
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
//        T.showLong(getActivity(), "当前选中:" + provinceName + "," + cityName + ","
//                + districtName + "," + areaCode);
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
