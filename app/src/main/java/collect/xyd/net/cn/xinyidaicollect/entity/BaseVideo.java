package collect.xyd.net.cn.xinyidaicollect.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @FileName:collect.xyd.net.cn.xinyidaicollect.entity.BaseVideo.java
 * @author:Phoenix
 * @date:2015-09-14 12:10
 * @Version:V1.0
 */
public class BaseVideo implements Parcelable {
    private static final String TAG = "BaseVideo";
    private int video_id;
    private String video_path;

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.video_id);
        dest.writeString(this.video_path);
    }

    public BaseVideo() {
    }

    protected BaseVideo(Parcel in) {
        this.video_id = in.readInt();
        this.video_path = in.readString();
    }

    public static final Parcelable.Creator<BaseVideo> CREATOR = new Parcelable.Creator<BaseVideo>() {
        public BaseVideo createFromParcel(Parcel source) {
            return new BaseVideo(source);
        }

        public BaseVideo[] newArray(int size) {
            return new BaseVideo[size];
        }
    };
}
