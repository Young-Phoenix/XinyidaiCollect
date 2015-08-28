package collect.xyd.net.cn.xinyidaicollect.entity;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by Administrator on 2015/7/17 0017.
 */
public class Photo {
    private String path;
    private File file;
    private Bitmap bitmap;

    public Photo(String path, Bitmap bitmap) {
        this.path = path;
        this.bitmap = bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
