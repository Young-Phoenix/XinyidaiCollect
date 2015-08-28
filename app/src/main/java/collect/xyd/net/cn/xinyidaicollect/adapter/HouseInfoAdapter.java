package collect.xyd.net.cn.xinyidaicollect.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.entity.HouseInfoV2;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.TimeUtils;

/**
 * Created by Administrator on 2015/8/4 0004.
 */
public class HouseInfoAdapter extends InfoAdapter<HouseInfoV2>{

   /* private DisplayImageOptions options;
    private Context mContext;*/
    public HouseInfoAdapter(Context context){
        super(context);
        //this.mContext = context;
        /*this.mInflater = LayoutInflater.from(mContext);
        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.qian_default_image) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.qian_default_image) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.qian_default_image) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象*/
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position,convertView,parent);
        HouseInfoV2 houseInfo = getItem(position);
        if(houseInfo!=null){
            if(houseInfo.getImages()!=null && houseInfo.getImages().size()>0){
                ImageLoader.getInstance().displayImage(Constants.SERVER_HOUSE_IP+houseInfo.getImages().get(0).getImg_path(),
                        viewHolder.pictureView, options,
                        new AnimateFirstDisplayListener());
            }else{
                viewHolder.pictureView.setImageResource(R.mipmap.qian_default_image);
            }
            viewHolder.title.setText(houseInfo.getTitle());
            viewHolder.type.setText(houseInfo.getHuxing() + "-" + houseInfo.getAddress());
            viewHolder.price.setText(houseInfo.getShoujia() + (houseInfo.getCate()==1?"元":"万"));
            viewHolder.addtime.setText(TimeUtils.longToStrng(houseInfo.getTime()));
        }
        return convertView;
    }


    private static class AnimateFirstDisplayListener extends
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
}
