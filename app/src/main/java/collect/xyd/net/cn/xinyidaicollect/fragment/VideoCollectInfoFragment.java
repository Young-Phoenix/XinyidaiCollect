package collect.xyd.net.cn.xinyidaicollect.fragment;

import android.app.Activity;
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
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.net.xyd.http.HttpRequestUtil;
import collect.xyd.net.cn.xinyidaicollect.App;
import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.entity.VideoInfo;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.FileNameUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * 需要拍照和拍视频的fragment继承
 * Created by Administrator on 2015/7/23 0023.
 */
public class VideoCollectInfoFragment extends PhotoCollectInfoFragment{
    public static final int REQUEST_CODE_CAPTURE_INNER_VIDEO = 3;//拍视频
    public static final int REQUEST_CODE_PICK_INNER_VIDEO = 4;//从手机选择
    public static final int REQUEST_CODE_CAPTURE_OUTER_VIDEO = 31;//拍视频
    public static final int REQUEST_CODE_PICK_OUTER_VIDEO = 41;//从手机选择
    protected static final int DELETE_INNER = 100;
    protected static final int DELETE_OUTER = 101;
    protected String innerVideoPath = null;
    protected String outerVideoPath = null;


    protected void videoListener(final View v, final int captureRequestCode, final int pickRequestCode) {
        PopupMenu pop = new PopupMenu(getActivity(), v);
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.take_video:
                        T.showLong(getActivity(), R.string.video_record_notice_info);
                        Intent takeIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        takeIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        String videoPath = null;
                        if(v.getId()==R.id.btn_add_inner_video){
                            videoPath = innerVideoPath = FileNameUtil.getInstance().getFilePath(getActivity()) + "VIDEO_" + FileNameUtil.getInstance().getFileName("mp4");
                        }else{
                            videoPath = outerVideoPath = FileNameUtil.getInstance().getFilePath(getActivity()) + "VIDEO_" + FileNameUtil.getInstance().getFileName("mp4");
                        }
                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoPath);
                        //takeIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sizeLimit);
                        //takeIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit);
                        startActivityForResult(takeIntent, captureRequestCode);
                        break;
                    case R.id.select_video:
                        T.showLong(getActivity(), R.string.video_select_notice_info);
                        Intent selectIntent = new Intent(Intent.ACTION_PICK);
                        selectIntent.setType("video/*");//视频类型
                        startActivityForResult(selectIntent, pickRequestCode);
                        break;
                }
                return true;
            }
        });
        pop.getMenuInflater().inflate(R.menu.popup_menu_video, pop.getMenu());
        pop.show();
    }
    protected void processVideo(Intent data, int requestCode, ImageView imageView, Button button) {
        if(data==null){
            captureVideoResult(requestCode, imageView, button);
            return;
        }
        Uri uri = data.getData();
        if (uri==null){
            captureVideoResult(requestCode, imageView, button);
            return;
        }
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
                        } else if (requestCode == REQUEST_CODE_CAPTURE_OUTER_VIDEO || requestCode == REQUEST_CODE_PICK_OUTER_VIDEO) {
                            outerVideoPath = video_path;
                        }
                        showVideoImage(video_path,imageView,button);
                    }
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    T.showLong(getActivity(), "视频文件不存在");
                } catch (IOException e) {
                    //e.printStackTrace();
                    T.showLong(getActivity(), "读取视频文件异常");
                }
            }
        }else{
            captureVideoResult(requestCode, imageView, button);
        }
    }
    private void captureVideoResult(int requestCode,ImageView imageView,Button button){
        String video_path=null;
        if (requestCode == REQUEST_CODE_CAPTURE_INNER_VIDEO || requestCode == REQUEST_CODE_PICK_INNER_VIDEO) {
            video_path = innerVideoPath;
        } else if (requestCode == REQUEST_CODE_CAPTURE_OUTER_VIDEO || requestCode == REQUEST_CODE_PICK_OUTER_VIDEO) {
            video_path = outerVideoPath;
        }
        if (video_path!=null && new File(video_path).exists()) {
            showVideoImage(video_path, imageView, button);
        }else{
            T.showLong(getActivity(), "视频文件不存在");
        }
    }

    private void showVideoImage(String video_path,ImageView imageView,Button button){
        //ThumbnailUtils类2.2以上可用
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(video_path, MediaStore.Video.Thumbnails.MICRO_KIND);
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
        button.setVisibility(View.GONE);
    }

    protected class VideoImageClickListener implements View.OnClickListener {
        private String videoPath;
        public VideoImageClickListener(String videoPath){
            this.videoPath = videoPath;
        }
        @Override
        public void onClick(View v) {

            if (videoPath != null) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String strend = "";
                    if (videoPath.toLowerCase().endsWith(".mp4")) {
                        strend = "mp4";
                    } else if (videoPath.toLowerCase().endsWith(".3gp")) {
                        strend = "3gp";
                    } else if (videoPath.toLowerCase().endsWith(".mov")) {
                        strend = "mov";
                    } else if (videoPath.toLowerCase().endsWith(".wmv")) {
                        strend = "wmv";
                    }
                    Uri uri = Uri.parse(videoPath);
                    intent.setDataAndType(uri, "video/" + strend);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    T.showLong(getActivity(), R.string.not_found_player);
                }
            }
        }
    }
    protected class VideoImageLongClickListener implements View.OnLongClickListener{
        private int operateType;
        private int infoType;
        private ImageView videoImage;
        private Button button;
        private String uri;
        private int witch;
        public VideoImageLongClickListener(int witch,int operateType,int infoType,ImageView imageView,Button button,String uri){
            this.witch = witch;
            this.operateType = operateType;
            this.infoType = infoType;
            this.videoImage = imageView;
            this.button = button;
            this.uri = uri;
        }
        @Override
        public boolean onLongClick(final View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.are_you_sure_delete);
            builder.setTitle(R.string.notice);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (operateType == 2) {
                        removeVideoFromServer((VideoInfo) v.getTag(), Constants.DELETE_VIDEO, infoType, uri);
                    }
                    if (witch == DELETE_INNER) {
                        innerVideoPath = null;
                    } else if (witch == DELETE_OUTER) {
                        outerVideoPath = null;
                    }

                    videoImage.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);
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
    }
    private void removeVideoFromServer(VideoInfo videoInfo, int requestCode,int type,String uri) {
        Map params = new HashMap();
        params.put(Constants.TOKEN, ((App) getActivity().getApplication()).user.getToken());
        params.put(Constants.TYPE, type + "");
        //params.put("che_id", videoInfo.getChe_id() + "");
        params.put("video_id", videoInfo.getVideo_id() + "");
        new HttpRequestUtil(this).stringRequest(requestCode, uri, params);
    }


}
