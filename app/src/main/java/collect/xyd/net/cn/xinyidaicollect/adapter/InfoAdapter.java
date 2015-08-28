package collect.xyd.net.cn.xinyidaicollect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import collect.xyd.net.cn.xinyidaicollect.R;

/**
 * Created by Administrator on 2015/8/7 0007.
 */
public abstract class InfoAdapter<T> extends BaseAdapter {
    protected DisplayImageOptions options;
    protected Context mContext;
    protected ViewHolder viewHolder = null;
    protected List<T> infos;
    private LayoutInflater mInflater;
    public InfoAdapter(Context context){
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
    public void setInfos(List<T> infos) {
        this.infos = infos;
    }
    @Override
    public int getCount() {
        return infos != null?infos.size():0;
    }

    @Override
    public T getItem(int position) {
        return infos != null ? infos.get(position):null;
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
        return convertView;
    }
    protected final class ViewHolder {
        protected ImageView pictureView;
        protected TextView title;
        protected TextView price;
        protected TextView type;
        protected TextView addtime;
    }
}
