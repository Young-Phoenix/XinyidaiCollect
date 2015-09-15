package collect.xyd.net.cn.xinyidaicollect.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @FileName:collect.xyd.net.cn.xinyidaicollect.entity.BusinessVideo.java
 * @author:Phoenix
 * @date:2015-09-14 12:15
 * @Version:V1.0
 */
public class BusinessVideo extends BaseVideo implements Parcelable {
    private static final String TAG = "BusinessVideo";
    private int agent_id;
    private int type;

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.agent_id);
        dest.writeInt(this.type);
    }

    public BusinessVideo() {
    }

    protected BusinessVideo(Parcel in) {
        this.agent_id = in.readInt();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<BusinessVideo> CREATOR = new Parcelable.Creator<BusinessVideo>() {
        public BusinessVideo createFromParcel(Parcel source) {
            return new BusinessVideo(source);
        }

        public BusinessVideo[] newArray(int size) {
            return new BusinessVideo[size];
        }
    };
}
