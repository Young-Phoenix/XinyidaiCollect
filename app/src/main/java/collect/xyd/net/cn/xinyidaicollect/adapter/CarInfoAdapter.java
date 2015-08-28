package collect.xyd.net.cn.xinyidaicollect.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.entity.CarInfo;
import collect.xyd.net.cn.xinyidaicollect.utils.Constants;
import collect.xyd.net.cn.xinyidaicollect.utils.TimeUtils;

/**
 * Created by Administrator on 2015/8/4 0004.
 */
public class CarInfoAdapter extends BaseAdapter {
    private DisplayImageOptions options;
    private Context mContext;
    private List<CarInfo>  cars;
    private LayoutInflater mInflater;
    private ViewHolder viewHolder = null;
    public CarInfoAdapter(Context context){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.qian_default_image) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.qian_default_image) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.qian_default_image) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象
    }

    public void setCars(List<CarInfo> cars) {
        this.cars = cars;
    }

    @Override
    public int getCount() {
        return cars != null?cars.size():0;
    }

    @Override
    public Object getItem(int position) {
        return cars != null ? cars.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.info_list_item,null);
            viewHolder.pictureView = (ImageView)convertView.findViewById(R.id.iv_picture);
            viewHolder.title = (TextView)convertView.findViewById(R.id.tv_title);
            viewHolder.type = (TextView)convertView.findViewById(R.id.tv_type);
            viewHolder.price = (TextView)convertView.findViewById(R.id.tv_price);
            viewHolder.addtime = (TextView)convertView.findViewById(R.id.tv_add_time);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        CarInfo carInfo = (CarInfo)getItem(position);
        if(carInfo!=null){
            if(carInfo.getImages()!=null && carInfo.getImages().size()>0){
                    ImageLoader.getInstance().displayImage(Constants.SERVER_CAR_IP+carInfo.getImages().get(0).getImg_path(),
                            viewHolder.pictureView, options,
                            new AnimateFirstDisplayListener());
            }else{
                viewHolder.pictureView.setImageResource(R.mipmap.qian_default_image);
            }
            viewHolder.title.setText(carInfo.getTitle());
            viewHolder.type.setText(carInfo.getPinpai() + "-" + carInfo.getLicheng() + "万公里");
            viewHolder.price.setText(carInfo.getShoujia() + "万");
            viewHolder.addtime.setText(TimeUtils.longToStrng(carInfo.getTime()));
        }
        return convertView;
    }

    private final class ViewHolder {
        private ImageView pictureView;
        private TextView title;
        private TextView price;
        private TextView type;
        private TextView addtime;
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
