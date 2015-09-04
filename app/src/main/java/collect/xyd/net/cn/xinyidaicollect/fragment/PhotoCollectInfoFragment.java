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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.net.xyd.http.HttpRequestUtil;
import cn.net.xyd.view.CustomDialog;
import collect.xyd.net.cn.xinyidaicollect.App;
import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.entity.Photo;
import collect.xyd.net.cn.xinyidaicollect.entity.VideoInfo;
import collect.xyd.net.cn.xinyidaicollect.listener.CommitListener;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.FileNameUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.L;
import collect.xyd.net.cn.xinyidaicollect.utils.PictureUtil;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * 只需要拍照功能的fragment继承
 * Created by Administrator on 2015/7/23 0023.
 */
public abstract class PhotoCollectInfoFragment extends Fragment implements HttpRequestUtil.HttpRequestListener {
    public static final int REQUEST_CODE_CAPTURE_CAMEIA = 1;//拍照
    public static final int REQUEST_CODE_PICK_IMAGE = 2;//从相册选择
    protected CommitListener commitListener;
    protected LinearLayout llPhoto;
    protected Button btnAddPhoto;
    protected Photo photo;
    protected List<Photo> photos = new ArrayList<Photo>();
    private int images;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            commitListener = (CommitListener) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + "must implements CommitListener");
        }
    }

    /**
     * 添加照片监听
     * @param view
     */
    protected void photoListener(final View  view){
        PopupMenu pop = new PopupMenu(getActivity(), view);
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
                        Intent intent = new Intent();
                                /* 开启Pictures画面Type设定为image */
                        intent.setType("image/*");
                                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                /* 取得相片后返回本画面 */
                        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                        break;
                }
                return true;
            }
        });
        pop.getMenuInflater().inflate(R.menu.popup_menu, pop.getMenu());
        pop.show();
    }

    /**
     * 查看大图
     */
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

    /**
     * 处理照片选择事件
     */
    protected void processPickPhoto(Intent data){
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
                        Photo photoTemp = new Photo(img_path, null);
                        photoTemp.setFile(imageFile);
                        photos.add(photoTemp);
                        ImageView imageView = addOneImageView(photoTemp);
                        imageView.setImageBitmap(getPreview(img_path));
                        hiddenAddPhotoBtn();
                    }else{
                        T.showLong(getActivity(), "获取图片失败，请重新选择");
                    }
                }else{
                    T.showLong(getActivity(), "图片不存在");
                }
            } else {
                T.showLong(getActivity(), "获取不到图片");
            }
        }else{
            T.showLong(getActivity(), "获取不到图片");
        }
    }

    /**
     * 处理照片拍摄事件
     */
    protected void processCapturePhoto(){
        Bitmap bitmap = getPreview(photo.getPath());
        photo.setBitmap(bitmap);
        File imageFile = PictureUtil.writeImageFile2SDcard(getActivity(), photo.getPath());
        if(imageFile.exists()) {
            photo.setFile(imageFile);
            photos.add(photo);
            ImageView imageView = addOneImageView(photo);
            imageView.setImageBitmap(bitmap);
            hiddenAddPhotoBtn();
        }else{
            T.showLong(getActivity(), "获取照片失败，请重新拍摄");
        }
    }

    /**
     * 新增一个imageView
     */
    protected ImageView addOneImageView(Photo photo) {
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

    /**
     * 隐藏按钮
     */
    protected void hiddenAddPhotoBtn(){
        if(++images>=4){
            btnAddPhoto.setVisibility(View.GONE);
        }
    }
    /**
     * 显示添加按钮
     */
    private void showAddPhotoBtn(){
        if(--images<4 && btnAddPhoto.getVisibility() == View.GONE){
            btnAddPhoto.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 从给定路径中读取图片
     */
    protected Bitmap readBitmapFromPath(String path) {
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
    public void onResponse(int requestCode, String data) {

    }

    @Override
    public void onErrorResponse(int requestCode, String error) {

    }

    protected static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(loadedImage);
                // 是否第一次显示
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // 图片淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    protected Bitmap getPreview(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); //此时返回bm为空
        options.inJustDecodeBounds = false;
        //计算缩放比
        int be = (int) (options.outHeight / (float) 200);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        return bitmap;
    }
}
