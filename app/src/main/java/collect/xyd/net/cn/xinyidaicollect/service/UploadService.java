package collect.xyd.net.cn.xinyidaicollect.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import collect.xyd.net.cn.xinyidaicollect.MainActivity;
import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.entity.BusinessImage;
import collect.xyd.net.cn.xinyidaicollect.entity.RequestResult;
import collect.xyd.net.cn.xinyidaicollect.entity.User;
import collect.xyd.net.cn.xinyidaicollect.fragment.CarInfoFragment;
import collect.xyd.net.cn.xinyidaicollect.fragment.HouseInfoFragmentV2;
import collect.xyd.net.cn.xinyidaicollect.fragment.ShopBusinessFragment;
import collect.xyd.net.cn.xinyidaicollect.json.JsonUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.MultipartUtility;
import collect.xyd.net.cn.xinyidaicollect.utils.PictureUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * @FileName:collect.xyd.net.cn.xinyidaicollect.service.UploadService.java
 * @author:Phoenix
 * @date:2015-09-14 15:08
 * @Version:V1.0
 */
public class UploadService extends Service{
    private static final String TAG = "UploadService";
    private NotificationManager mNM;
    @Override
    public void onCreate() {
        super.onCreate();
        mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification(){
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.upload_notify);
        builder.setContentTitle("正在上传……")
                .setContent(view)
                .setTicker("开始上传")
                .setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis())
                        //.setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                        //.setOngoing(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.getNotification();

        // 注意使用  startForeground ，id 为 0 将不会显示 notification
        startForeground(1, notification);
        mNM.notify(1, notification);
    }

    private class UploadUtil extends AsyncTask<Map<String, Map>, Integer, List> {
        @Override
        protected List doInBackground(Map<String, Map>... params) {
            Map<String, String> base_config = params[0].get("base_config");
            Map<String, String> form_field = params[0].get("form_field");
            Map<String, File> file_part = params[0].get("file_part");
            MultipartUtility multipart = null;
            try {
                multipart = new MultipartUtility(base_config.get("url"), base_config.get("charset"));
                multipart.addHeaderField("User-Agent", "WinHttpClient");
                multipart.addHeaderField("Test-Header", "Header-Value");
                for (Map.Entry<String, String> entry : form_field.entrySet()) {
                    multipart.addFormField(entry.getKey(), entry.getValue());
                }
                if(file_part!=null) {
                    for (Map.Entry<String, File> entry : file_part.entrySet()) {
                        multipart.addFilePart(entry.getKey(), entry.getValue());
                    }
                }
                List<String> response = multipart.finish();
                StringBuilder result = new StringBuilder();
                for (String msg : response) {
                    result.append(msg);
                }
                List resultList = new ArrayList();
                resultList.add(result.toString());
                resultList.add(file_part);
                return resultList;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            showNotification();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List resultList) {
            if (resultList!=null && resultList.size() > 0) {
                JsonUtil<User> jsonUtil = new JsonUtil<User>();
                RequestResult<User> commitResult = jsonUtil.json2Obj(resultList.get(0).toString(), new TypeReference<RequestResult<User>>() {
                });
                if(commitResult!=null) {
                    switch (commitResult.getMsg().getResultCode()) {
                        case 200:
                            if (resultList.get(1) != null) {
                                for (Map.Entry<String, File> entry : ((Map<String, File>) (resultList.get(1))).entrySet()) {
                                    if (entry.getKey().startsWith("image")) {
                                        PictureUtil.deleteTempFile(entry.getValue().getAbsolutePath());
                                    }
                                }
                            }
                            break;
                        case 400:
                           //commitResult.getMsg().getMessage()
                            break;
                    }
                }else{
                    //"提交失败"
                }
            } else {
                //"连接超时，请重试"
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    IUploadService.Stub binder = new IUploadService.Stub(){

        @Override
        public void upload(BusinessInfo info,String uri) throws RemoteException {
            if(info!=null && uri!=null){
                Map<String,String> fieldMap = new HashMap<String, String>();
                Map<String,File> fileMap = new HashMap<String, File>();
                Map<String, String> base_config = new HashMap<String,String>();
                fieldMap.put("token",info.getUser_id()+"");
                fieldMap.put("username", info.getUsername());
                fieldMap.put("password", info.getPassword());
                fieldMap.put("agent_name", info.getAgent_name());
                fieldMap.put("type", info.getType()+"");
                fieldMap.put("linkman", info.getLink_man());
                fieldMap.put("tel", info.getTel());
                fieldMap.put("mobile", info.getMobile());
                fieldMap.put("description", info.getDescription());
                fieldMap.put("address", info.getAddress());
                fieldMap.put(Constants.GEOLAT, info.getGps_x() + "");
                fieldMap.put(Constants.GEOLNG, info.getGps_y() + "");
                int size = info.getImages().size();
                for(int i=0;i<size;i++){
                    fileMap.put("image"+i,new File(info.getImages().get(i).getImg_path()));
                }
                fileMap.put("video_inside", new File(info.getVideo().get("video_inside").getVideo_path()));
                fileMap.put("video_outside", new File(info.getVideo().get("video_outside").getVideo_path()));

                base_config.put("url", Constants.SERVER_IP + uri);
                base_config.put("charset", "GBK");
                Map<String, Map> reqParams = new HashMap<String, Map>();
                reqParams.put("base_config", base_config);
                reqParams.put("form_field", fieldMap);
                reqParams.put("file_part", fileMap);

                new UploadUtil().execute(reqParams);
            }

        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
