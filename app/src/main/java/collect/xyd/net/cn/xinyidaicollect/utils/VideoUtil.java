package collect.xyd.net.cn.xinyidaicollect.utils;

import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Administrator on 2015/7/21 0021.
 */
public class VideoUtil {

    private static final String TAG = "VideoUtil";

    public static String videoToString(String filePath) {
        try {
            File tmpFile = new File(filePath);
            BufferedInputStream in = new BufferedInputStream(
                    new FileInputStream(tmpFile));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[in.available()];
            int length;
            while ((length = in.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            L.e("读取视频出现异常", e.getMessage());
        }
        return null;
    }
}
