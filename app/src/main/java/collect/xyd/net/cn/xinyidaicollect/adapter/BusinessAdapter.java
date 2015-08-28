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
import collect.xyd.net.cn.xinyidaicollect.entity.BusinessInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.CarInfo;

/**
 * Created by Administrator on 2015/8/4 0004.
 */
public class BusinessAdapter extends BaseAdapter {
    private Context mContext;
    private List<BusinessInfo>  businesses;
    private LayoutInflater mInflater;
    private ViewHolder viewHolder = null;
    public BusinessAdapter(Context context, List<BusinessInfo> businesses){
        this.mContext = context;
        this.businesses = businesses;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setBusinesses(List<BusinessInfo> businesses) {
        this.businesses = businesses;
    }

    @Override
    public int getCount() {
        return businesses != null?businesses.size():0;
    }

    @Override
    public Object getItem(int position) {
        return businesses != null ? businesses.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.car_business_list_item,null);
            viewHolder.name = (TextView)convertView.findViewById(R.id.tv_business_name);
            viewHolder.address = (TextView)convertView.findViewById(R.id.tv_business_address);
            viewHolder.tel = (TextView)convertView.findViewById(R.id.tv_business_tel);
            viewHolder.distance = (TextView)convertView.findViewById(R.id.tv_distance);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        BusinessInfo businessInfo = (BusinessInfo)getItem(position);
        if(businessInfo!=null){
            viewHolder.name.setText(businessInfo.getAgent_name());
            viewHolder.address.setText(businessInfo.getAddress());
            viewHolder.tel.setText(businessInfo.getTel());
            viewHolder.distance.setText(businessInfo.getDistance()+"M");
        }
        return convertView;
    }

    private final class ViewHolder {
        private TextView name;
        private TextView address;
        private TextView tel;
        private TextView distance;
    }
}
