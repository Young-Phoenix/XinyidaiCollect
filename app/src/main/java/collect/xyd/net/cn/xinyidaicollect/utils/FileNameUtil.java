package collect.xyd.net.cn.xinyidaicollect.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015/7/17 0017.
 */
public class FileNameUtil {
    private SimpleDateFormat sdf;
    private static FileNameUtil fileNameUtil;
    public static FileNameUtil getInstance(){
        if(fileNameUtil==null){
            fileNameUtil = new FileNameUtil();
        }
        return fileNameUtil;
    }
    private FileNameUtil(){
        sdf = new SimpleDateFormat("yyyyMMddhhmmss",
                Locale.SIMPLIFIED_CHINESE);
    }

    public String getFileName(String extension){
        return sdf.format(new Date()) +"."+ extension;
    }
    public String getFilePath(Context context){
        return Environment.getExternalStorageDirectory()+File.separator+context.getPackageName()+File.separator;
    }

}
