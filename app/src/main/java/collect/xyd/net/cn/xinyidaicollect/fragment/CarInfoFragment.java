package collect.xyd.net.cn.xinyidaicollect.fragment;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.golshadi.majid.core.DownloadManagerPro;
import com.golshadi.majid.report.listener.DownloadManagerListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.net.xyd.http.HttpRequestUtil;
import cn.net.xyd.http.RequestUri;
import cn.net.xyd.view.CustomDialog;
import collect.xyd.net.cn.xinyidaicollect.App;
import collect.xyd.net.cn.xinyidaicollect.AudioRecordActivity;
import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.entity.AudioInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.CarImageInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.CarInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.Photo;
import collect.xyd.net.cn.xinyidaicollect.entity.VideoInfo;
import collect.xyd.net.cn.xinyidaicollect.mp3lame.MyAudioRecordActivity;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.FileNameUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.L;
import collect.xyd.net.cn.xinyidaicollect.utils.PictureUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.RecordUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.SPUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.T;
import collect.xyd.net.cn.xinyidaicollect.utils.TimeUtils;

/**
 * Created by Administrator on 2015/7/17 0017.
 */
public class CarInfoFragment extends CollectInfoFragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    public static final int AUDIO_DURATION_LIMIT = 30;//音频限制30秒
    @Bind(R.id.et_car_title)
    EditText etCarTitle;
    @Bind(R.id.et_car_price)
    EditText etCarPrice;
    @Bind(R.id.et_car_age)
    EditText etCarAge;
    @Bind(R.id.bs_car_type)
    BetterSpinner bsCarType;
    @Bind(R.id.et_car_brand)
    EditText etCarBrand;
    @Bind(R.id.et_contacts)
    EditText etContacts;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_car_licences_time)
    EditText etCarLicencesTime;
    @Bind(R.id.et_car_miles)
    EditText etCarMiles;
    @Bind(R.id.et_car_emission)
    EditText etCarEmission;
    @Bind(R.id.et_car_color)
    EditText etCarColor;
    @Bind(R.id.et_car_check_time)
    EditText etCarCheckTime;
    @Bind(R.id.et_car_insurance_time)
    EditText etCarInsuranceTime;
    @Bind(R.id.et_car_accident)
    EditText etCarAccident;
    @Bind(R.id.et_car_description)
    EditText etCarDescription;
    @Bind(R.id.ll_photo)
    LinearLayout llPhoto;
    @Bind(R.id.btn_add_photo)
    Button btnAddPhoto;
    @Bind(R.id.btn_publish)
    Button btnPublish;
    public static final String ARGUMENT = "argument";
    @Bind(R.id.tv_audio_text)
    TextView tvAudioText;
    @Bind(R.id.btn_add_audio)
    Button btnAddAudio;
    @Bind(R.id.circularButton_audio)
    CircularProgressButton circularButtonAudio;
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


    /**
     * 传入需要的参数，设置给arguments
     *
     * @param argument
     * @return
     */
    public static CarInfoFragment newInstance(Serializable argument, int operate_type) {
        operateType = operate_type;
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGUMENT, argument);
        CarInfoFragment contentFragment = new CarInfoFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    private static int operateType = 1;//1是添加，2是修改
    private CarInfo carInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.car_info_layout, container, false);
        ButterKnife.bind(this, view);
        initData(savedInstanceState);
        initListener();
        return view;
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }
        }
        String[] list = getResources().getStringArray(R.array.spinner_car_type);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, list);

        bsCarType.setAdapter(adapter);
        if (operateType == 2 && getArguments() != null) {
            this.options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.qian_default_image) // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.qian_default_image) // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.qian_default_image) // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                    .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                    .build(); // 创建配置过得DisplayImageOption对象
            carInfo = (CarInfo) getArguments().getSerializable(ARGUMENT);
            btnPublish.setText("提交");
            etCarTitle.setText(carInfo.getTitle());
            etCarPrice.setText(carInfo.getShoujia() + "");
            etCarAge.setText(carInfo.getCheling() + "");
            bsCarType.setText(carInfo.getChe_type());
            etCarBrand.setText(carInfo.getPinpai());
            etContacts.setText(carInfo.getLianxiren());
            etAddress.setText(carInfo.getAddress());
            etPhone.setText(carInfo.getTel());
            etCarLicencesTime.setText(TimeUtils.longToStrng(carInfo.getShangpaitime()));
            etCarMiles.setText(carInfo.getLicheng() + "");
            etCarEmission.setText(carInfo.getPailiang() + "");
            etCarColor.setText(carInfo.getColor());
            etCarCheckTime.setText(carInfo.getNianjian_endtime());
            etCarInsuranceTime.setText(carInfo.getQiangxian());
            etCarAccident.setText(carInfo.getShigu());
            etCarDescription.setText(carInfo.getDetail_description());
            if (carInfo.getImages() != null) {
                for (int i = 0; i < carInfo.getImages().size(); i++) {
                    addImageView(i + 1, llPhoto, carInfo.getImages().get(i));
                }
            }
            if(carInfo.getImages().size()>=4){
                btnAddPhoto.setVisibility(View.GONE);
            }
            VideoInfo video_inside;

            if (carInfo.getVideo() != null && (video_inside = carInfo.getVideo().get("video_inside")) != null && !TextUtils.isEmpty(video_inside.getVideo_path())) {
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
            VideoInfo video_outside;
            if (carInfo.getVideo() != null && (video_outside = carInfo.getVideo().get("video_outside")) != null && !TextUtils.isEmpty(video_outside.getVideo_path())) {
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
            if (carInfo.getAudio() != null && !TextUtils.isEmpty(carInfo.getAudio().getAudio_path())) {
                circularButtonAudio.setVisibility(View.VISIBLE);
                tvAudioText.setTag(carInfo.getAudio());
                btnAddAudio.setVisibility(View.GONE);
                final String httpAudioPath = carInfo.getAudio().getAudio_path();
                String[] files = httpAudioPath.split("/");
                downAudioFileName = files[files.length - 1];
                String fileName = downAudioFileName.split("\\.")[0];
                circularButtonAudio.setIndeterminateProgressMode(true);
                circularButtonAudio.setOnClickListener(new DownloadClickListener(httpAudioPath, fileName, 2, 3));
            }
        }else{
            etAddress.setText(SPUtils.get(getActivity(),Constants.KEY_ADDRESS,"").toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_inner_video:
                videoListener(v, REQUEST_CODE_CAPTURE_INNER_VIDEO, REQUEST_CODE_PICK_INNER_VIDEO);
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

                int taskToken = dm.addTask(downFileName, Constants.SERVER_CAR_IP + httpAudioPath, true, false);
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
                case 2:
                    circularButtonAudio.setProgress(msg.arg1);
                    break;
                case 3:
                    audioPath = basePath + downAudioFileName;
                    tvAudioText.setText(new File(audioPath).getName());
                    tvAudioText.setVisibility(View.VISIBLE);
                    circularButtonAudio.setVisibility(View.GONE);
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
    private String downAudioFileName;
    private DisplayImageOptions options;

    //新增一个imageView
    private ImageView addImageView(int viewId, ViewGroup parent, final CarImageInfo imageInfo) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setId(viewId);
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
                        carInfo.getImages().remove(imageInfo);
                        v.setVisibility(View.GONE);
                        if (btnAddPhoto.getVisibility() == View.GONE) {
                            btnAddPhoto.setVisibility(View.VISIBLE);
                        }
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
        ImageLoader.getInstance().displayImage(Constants.SERVER_CAR_IP + imageInfo.getImg_path(),
                imageView, options,
                new AnimateFirstDisplayListener());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBigImg(imageInfo);
            }
        });
        return imageView;
    }

    private static final int DELETE_CAR_IMAGE = 1;
    private static final int DELETE_CAR_VIDEO = 2;
    private static final int DELETE_CAR_AUDIO = 3;

    private void removeImageFromServer(CarImageInfo carImageInfo, int requestCode) {
        Map params = new HashMap();
        params.put(Constants.TOKEN, ((App) getActivity().getApplication()).user.getToken());
        params.put(Constants.TYPE, DELETE_CAR_IMAGE + "");
        //params.put("che_id", carImageInfo.getChe_id() + "");
        params.put("img_id", carImageInfo.getImg_id() + "");
        new HttpRequestUtil(this).stringRequest(requestCode, RequestUri.DELETE_CAR_FILE, params);
    }

    private void removeVideoFromServer(VideoInfo videoInfo, int requestCode) {
        Map params = new HashMap();
        params.put(Constants.TOKEN, ((App) getActivity().getApplication()).user.getToken());
        params.put(Constants.TYPE, DELETE_CAR_VIDEO + "");
        //params.put("che_id", videoInfo.getChe_id() + "");
        params.put("video_id", videoInfo.getVideo_id() + "");
        new HttpRequestUtil(this).stringRequest(requestCode, RequestUri.DELETE_CAR_FILE, params);
    }

    private void removeAudioFromServer(AudioInfo audioInfo, int requestCode) {
        Map params = new HashMap();
        params.put(Constants.TOKEN, ((App) getActivity().getApplication()).user.getToken());
        params.put(Constants.TYPE, DELETE_CAR_AUDIO + "");
        //params.put("che_id", audioInfo.getChe_id() + "");
        params.put("audio_id", audioInfo.getAudio_id() + "");
        new HttpRequestUtil(this).stringRequest(requestCode, RequestUri.DELETE_CAR_FILE, params);
    }

    private void showBigImg(CarImageInfo imageInfo) {
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
        ImageLoader.getInstance().displayImage(Constants.SERVER_CAR_IP + imageInfo.getImg_path(),
                imageView, options,
                new AnimateFirstDisplayListener());
    }

    public static final String DATEPICKER_TAG = "datepicker";
    private EditText currentET;

    @OnClick({R.id.et_car_licences_time, R.id.et_car_check_time, R.id.et_car_insurance_time})
    public void onSelectTime(View view) {
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                false);
        datePickerDialog.setVibrate(false);
        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.setCloseOnSingleTapDay(true);
        datePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
        currentET = (EditText) view;
    }

    private void initListener() {

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
                                    T.showLong(getActivity(), R.string.sdcard_notice_info);
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
        btnAddInnerVideo.setOnClickListener(this);
        btnAddOuterVideo.setOnClickListener(this);
        //录音
        btnAddAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getActivity(), v);
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.take_audio:
                                try {
                                    Intent takeIntent = new Intent(getActivity(), MyAudioRecordActivity.class);
                                    startActivityForResult(takeIntent,REQUEST_CODE_RECORD_AUDIO);
                                    //takeIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, AUDIO_DURATION_LIMIT);
                                    //takeIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sizeLimit);
                                    //Intent takeIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                                    //startActivityForResult(takeIntent, REQUEST_CODE_RECORD_AUDIO);
                                /*Intent takeIntent = new Intent(getActivity(), AudioRecordActivity.class);
                                startActivityForResult(takeIntent,REQUEST_CODE_RECORD_AUDIO);*/
                                } catch (Exception e) {
                                    T.showLong(getActivity(), "系统没有录音机");
                                }
                                break;
                           /* case R.id.select_audio:
                                T.showLong(getActivity(), "请选择小于2M的音频文件");
                                *//*Intent selectIntent = new Intent(Intent.ACTION_PICK);
                                selectIntent.setType("audio*//**//**//**//*");//音频类型*//*
                                Intent selectIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                selectIntent.setType("audio*//*");//音频类型
                                startActivityForResult(selectIntent, REQUEST_CODE_PICK_AUDIO);
                                break;*/
                        }
                        return true;
                    }
                });
                pop.getMenuInflater().inflate(R.menu.popup_menu_audio, pop.getMenu());
                pop.show();
            }
        });
        /*btnAddAudio.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {
                   String path = FileNameUtil.getInstance().getFilePath(getActivity());
                   String fileName = FileNameUtil.getInstance().getFileName("mp3");
                   if (recordAudio == null) {
                       recordAudio = new RecordAudio(path, fileName);
                   }
                   Player player = null;
                   if ("stop".equals(v.getTag())) {
                       recordAudio.stopRecorder();
                       btnAddAudio.setText("播放");
                       btnAddAudio.setTag("play");
                       tvAudioText.setText(fileName);
                       tvAudioText.setVisibility(View.VISIBLE);
                       timer.cancel();
                   } else if ("play".equals(v.getTag())) {
                       player = new Player(path + fileName);
                       player.play();
                       btnAddAudio.setText("停止");
                       btnAddAudio.setTag("stop_play");
                       timer = new Timer(true);
                       timer.schedule(task, 0, 1000);
                   } else if ("stop_play".equals(v.getTag())) {
                       if (player != null) {
                           player.stop();
                       }
                       timer.cancel();
                   } else {
                       timer.schedule(task, 0, 1000);
                       recordAudio.startRecorder();
                       btnAddAudio.setText("停止");
                       btnAddAudio.setTag("stop");

                   }
               }

           }

        );*/
        ivInnerVideoImage.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (innerVideoPath != null) {
                                                         try {
                                                             Intent intent = new Intent(Intent.ACTION_VIEW);
                                                             String strend = "";
                                                             if (innerVideoPath.toLowerCase().endsWith(".mp4")) {
                                                                 strend = "mp4";
                                                             } else if (innerVideoPath.toLowerCase().endsWith(".3gp")) {
                                                                 strend = "3gp";
                                                             } else if (innerVideoPath.toLowerCase().endsWith(".mov")) {
                                                                 strend = "mov";
                                                             } else if (innerVideoPath.toLowerCase().endsWith(".wmv")) {
                                                                 strend = "wmv";
                                                             }
                                                             Uri uri = Uri.parse(innerVideoPath);
                                                             intent.setDataAndType(uri, "video/" + strend);
                                                             startActivity(intent);
                                                         } catch (ActivityNotFoundException e) {
                                                             T.showLong(getActivity(), R.string.not_found_player);
                                                         }
                                                     }
                                                 }
                                             }

        );
        ivOuterVideoImage.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (outerVideoPath != null) {
                                                         try {
                                                             Intent intent = new Intent(Intent.ACTION_VIEW);
                                                             String strend = "";
                                                             if (outerVideoPath.toLowerCase().endsWith(".mp4")) {
                                                                 strend = "mp4";
                                                             } else if (outerVideoPath.toLowerCase().endsWith(".3gp")) {
                                                                 strend = "3gp";
                                                             } else if (outerVideoPath.toLowerCase().endsWith(".mov")) {
                                                                 strend = "mov";
                                                             } else if (outerVideoPath.toLowerCase().endsWith(".wmv")) {
                                                                 strend = "wmv";
                                                             }
                                                             Uri uri = Uri.parse(outerVideoPath);
                                                             intent.setDataAndType(uri, "video/" + strend);
                                                             startActivity(intent);
                                                         } catch (ActivityNotFoundException e) {
                                                             T.showLong(getActivity(), R.string.not_found_player);
                                                         }
                                                     }
                                                 }
                                             }

        );
        tvAudioText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioPath != null) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(audioPath);
                        intent.setDataAndType(uri, "audio/*");
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        T.showLong(getActivity(), R.string.not_found_player);
                    }
                }
            }
        });
        ivInnerVideoImage.setOnLongClickListener(new VideoImageLongClickListener(DELETE_INNER,operateType, DELETE_CAR_VIDEO, ivInnerVideoImage, btnAddInnerVideo, RequestUri.DELETE_CAR_FILE));
        ivOuterVideoImage.setOnLongClickListener(new VideoImageLongClickListener(DELETE_OUTER,operateType, DELETE_CAR_VIDEO, ivOuterVideoImage, btnAddOuterVideo, RequestUri.DELETE_CAR_FILE));
        tvAudioText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.are_you_sure_delete);
                builder.setTitle(R.string.notice);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (operateType == 2) {
                            removeAudioFromServer((AudioInfo) v.getTag(), Constants.DELETE_AUDIO);
                        }
                        audioPath = null;
                        tvAudioText.setVisibility(View.GONE);
                        btnAddAudio.setVisibility(View.VISIBLE);
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
                                                      if ((imageFile = photos.get(i).getFile()) != null) {
                                                          fileMap.put("image" + i, imageFile);
                                                      }
                                                  }
                                                  if (!TextUtils.isEmpty(innerVideoPath)) {
                                                      fileMap.put("video_inside", new File(innerVideoPath));
                                                  }
                                                  if (!TextUtils.isEmpty(outerVideoPath)) {
                                                      fileMap.put("video_outside", new File(outerVideoPath));
                                                  }
                                                  if (!TextUtils.isEmpty(audioPath)) {
                                                      fileMap.put("audio", new File(audioPath));
                                                  }

                                                  //文本信息
                                                  Map<String, String> params = new HashMap<String, String>();
                                                  if (operateType == 2) {
                                                      params.put("che_id", carInfo.getChe_id() + "");
                                                  }
                                                  params.put("car_title", etCarTitle.getText().toString());
                                                  params.put("car_price", etCarPrice.getText().toString());
                                                  params.put("car_age", etCarAge.getText().toString());
                                                  params.put("car_type", bsCarType.getText().toString());
                                                  params.put("car_brand", etCarBrand.getText().toString());
                                                  params.put("car_contacts", etContacts.getText().toString());
                                                  params.put("car_address", etAddress.getText().toString());
                                                  params.put("car_phone", etPhone.getText().toString());
                                                  params.put("car_licences_time", etCarLicencesTime.getText().toString());
                                                  params.put("car_miles", etCarMiles.getText().toString());
                                                  params.put("car_emission", etCarEmission.getText().toString());
                                                  params.put("car_color", etCarColor.getText().toString());
                                                  params.put("car_check_time", etCarCheckTime.getText().toString());
                                                  params.put("car_insurance_time", etCarInsuranceTime.getText().toString());
                                                  params.put("car_accident", etCarAccident.getText().toString());
                                                  params.put("car_description", etCarDescription.getText().toString());
                                                  if (operateType == 2) {
                                                      commitListener.commit(RequestUri.MODIFY_CAR_INFO, params, fileMap);
                                                  } else {

                                                      commitListener.commit(RequestUri.COMMIT_CAT_INFO, params, fileMap);
                                                  }
                                              }
                                          }
                                      }

        );
    }

    /*Timer timer = new Timer(true);
    RecordAudio recordAudio;
    TimerTask task = new TimerTask() {
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (time >= 30) {
                        recordAudio.stopRecorder();
                        btnAddAudio.setText("播放");
                        btnAddAudio.setTag("play");
                        timer.cancel();
                    } else {
                        time++;
                        tvAudioTime.setText(time + "''");
                    }
                }
            });
        }
    };*/
    private int time = 0;

    //表单验证信息
    private String validMsg;
    //存放被缩小后的图片

    private boolean validIsEmpty() {
        if (TextUtils.isEmpty(etCarTitle.getText())) {
            validMsg = "标题不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etCarPrice.getText())) {
            validMsg = "价格不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etCarAge.getText())) {
            validMsg = "车龄不能为空";
            return false;
        }
        if (TextUtils.isEmpty(bsCarType.getText())) {
            validMsg = "车辆类型不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etCarBrand.getText())) {
            validMsg = "车系品牌不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etContacts.getText())) {
            validMsg = "联系人不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etAddress.getText())) {
            validMsg = "地址不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etPhone.getText())) {
            validMsg = "电话不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etCarLicencesTime.getText())) {
            validMsg = "上牌时间不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etCarMiles.getText())) {
            validMsg = "里程不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etCarEmission.getText())) {
            validMsg = "排量不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etCarColor.getText())) {
            validMsg = "颜色不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etCarCheckTime.getText())) {
            validMsg = "年检截止时间不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etCarInsuranceTime.getText())) {
            validMsg = "强险到期时间不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etCarAccident.getText())) {
            validMsg = "是否事故车辆不能为空";
            return false;
        }
        if (TextUtils.isEmpty(etCarDescription.getText())) {
            validMsg = "车辆描述不能为空";
            return false;
        }
        if (photos.size() <= 0) {
            validMsg = "请添加照片";
            return false;
        }
        if (innerVideoPath == null) {
            validMsg = "请添加汽车内部视频";
            return false;
        }
        if (outerVideoPath == null) {
            validMsg = "请添加汽车外部视频";
            return false;
        }
        if (audioPath == null) {
            validMsg = "请添加音频";
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
                        File file = new File(img_path);
                        if (file != null && file.exists()) {
                            File imageFile = PictureUtil.writeImageFile2SDcard(getActivity(), img_path);
                            if(imageFile.exists()) {
                                Photo photoTemp = new Photo(img_path, null);
                                photoTemp.setFile(imageFile);
                                photos.add(photoTemp);
                                ImageView imageView = addOneImageView(photoTemp);
                                imageView.setImageBitmap(getPreview(img_path));
                                imageViews.add(imageView);
                                if (imageViews.size() == 4) {
                                    btnAddPhoto.setVisibility(View.GONE);
                                }
                            }else{
                                T.showLong(getActivity(), "获取图片失败，请重新选择");
                            }
                        } else {
                            T.showLong(getActivity(), "图片不存在");
                        }
                    } else {
                        T.showLong(getActivity(), "获取不到图片");
                    }

                }
            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {

                Bitmap bitmap = getPreview(photo.getPath());
                photo.setBitmap(bitmap);
                File imageFile = PictureUtil.writeImageFile2SDcard(getActivity(), photo.getPath());
                if(imageFile.exists()) {
                    photo.setFile(imageFile);
                    photos.add(photo);

                    ImageView imageView = addOneImageView(photo);
                    imageView.setImageBitmap(bitmap);

                    imageViews.add(imageView);

                    if (imageViews.size() == 4) {
                        btnAddPhoto.setVisibility(View.GONE);
                    }
                }else{
                    T.showLong(getActivity(), "获取照片失败，请重新拍摄");
                }

            } else if (requestCode == REQUEST_CODE_PICK_INNER_VIDEO || requestCode == REQUEST_CODE_CAPTURE_INNER_VIDEO) {
                processVideo(data, requestCode, ivInnerVideoImage, btnAddInnerVideo);
            } else if (requestCode == REQUEST_CODE_PICK_OUTER_VIDEO || requestCode == REQUEST_CODE_CAPTURE_OUTER_VIDEO) {
                processVideo(data, requestCode, ivOuterVideoImage, btnAddOuterVideo);
            } else if (requestCode == REQUEST_CODE_PICK_AUDIO) {
                processAudio(data);
            } else if (requestCode == REQUEST_CODE_RECORD_AUDIO) {
                //processAudio(data);
                processSelfAudio(data);
            }
        }
    }

    /*private void processVideo(Intent data, int requestCode) {
        Uri uri = data.getData();
        Cursor cursor = null;
        if (Build.VERSION.SDK_INT >= 17) {
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        } else {
            cursor = getActivity().managedQuery(uri, null, null, null, null);
        }
        if (cursor != null && cursor.moveToFirst()) {
            final String video_path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
            cursor.close();
            File file = new File(video_path);
            if (file.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    int length = fis.available();
                    if (length > 5 * 1024 * 1024) {
                        T.showLong(getActivity(), "视频不能超过5M,请在拍摄时选择最低画质");
                    } else {
                        if (requestCode == REQUEST_CODE_CAPTURE_INNER_VIDEO || requestCode == REQUEST_CODE_PICK_INNER_VIDEO) {
                            innerVideoPath = video_path;
                            //ThumbnailUtils类2.2以上可用
                            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(video_path, MediaStore.Video.Thumbnails.MICRO_KIND);
                            ivInnerVideoImage.setImageBitmap(bitmap);
                            ivInnerVideoImage.setVisibility(View.VISIBLE);
                            btnAddInnerVideo.setVisibility(View.GONE);
                        } else if (requestCode == REQUEST_CODE_CAPTURE_OUTER_VIDEO || requestCode == REQUEST_CODE_PICK_OUTER_VIDEO) {
                            outerVideoPath = video_path;
                            //ThumbnailUtils类2.2以上可用
                            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(video_path, MediaStore.Video.Thumbnails.MICRO_KIND);
                            ivOuterVideoImage.setImageBitmap(bitmap);
                            ivOuterVideoImage.setVisibility(View.VISIBLE);
                            btnAddOuterVideo.setVisibility(View.GONE);
                        }
                    }
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    T.showLong(getActivity(), "视频文件不存在");
                } catch (IOException e) {
                    //e.printStackTrace();
                    T.showLong(getActivity(), "读取视频文件异常");
                }
            }
            cursor.close();
        }

    }*/

    private String audioPath;

    private void processSelfAudio(Intent data){
        final Uri uri = data.getData();
        if (uri!=null) {
            String audio_path = uri.toString();
            File file = new File(audio_path);
            if (file.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    int length = fis.available();
                    if (length > 5 * 1024 * 1024) {
                        T.showLong(getActivity(), "音频不能超过5M");
                    } else {
                        audioPath = audio_path;
                        tvAudioText.setText(new File(audio_path).getName());
                        tvAudioText.setVisibility(View.VISIBLE);
                        btnAddAudio.setVisibility(View.GONE);
                    }
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    T.showLong(getActivity(), "音频文件不存在");
                } catch (IOException e) {
                    //e.printStackTrace();
                    T.showLong(getActivity(), "读取音频文件异常");
                }
            }
        }
    }

    private void processAudio(Intent data) {
        final Uri uri = data.getData();
        Cursor cursor = null;
        if (Build.VERSION.SDK_INT >= 17) {
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        } else {
            cursor = getActivity().managedQuery(uri, null, null, null, null);
        }
        if (cursor != null && cursor.moveToFirst()) {
            int i = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA);
            File file = new File(cursor.getString(i));
            cursor.close();
            String newFilePath = RecordUtils.getOutputMediaFile(RecordUtils.MEDIA_TYPE_AUDIO).getAbsolutePath().toString();
            File newFile;
            file.renameTo(newFile = new File(newFilePath));
            if (newFile.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(newFile);
                    int length = fis.available();
                    if (length > 5 * 1024 * 1024) {
                        T.showLong(getActivity(), "音频不能超过5M");
                    } else {
                        audioPath = newFilePath;
                        tvAudioText.setText(newFile.getName());
                        tvAudioText.setVisibility(View.VISIBLE);
                        btnAddAudio.setVisibility(View.GONE);
                    }
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    T.showLong(getActivity(), "音频文件不存在");
                } catch (IOException e) {
                    //e.printStackTrace();
                    T.showLong(getActivity(), "读取音频文件异常");
                }
            }
        }

        /*if (uri!=null) {
            String audio_path = uri.toString();
            File file = new File(audio_path);
            if (file.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    int length = fis.available();
                    if (length > 5 * 1024 * 1024) {
                        T.showLong(getActivity(), "音频不能超过5M");
                    } else {
                        audioPath = audio_path;
                        tvAudioText.setText(new File(audio_path).getName());
                        tvAudioText.setVisibility(View.VISIBLE);
                        btnAddAudio.setVisibility(View.GONE);
                    }
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    T.showLong(getActivity(), "音频文件不存在");
                } catch (IOException e) {
                    //e.printStackTrace();
                    T.showLong(getActivity(), "读取音频文件异常");
                }
            }
        }*/

    }
    private int id = 0;

    //新增一个imageView
    private ImageView addOneImageView(Photo photo) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setId(++id);
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
                        if (btnAddPhoto.getVisibility() == View.GONE) {
                            btnAddPhoto.setVisibility(View.VISIBLE);
                        }
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

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        currentET.setText(year + "-" + (month + 1) + "-" + day);
    }

}
