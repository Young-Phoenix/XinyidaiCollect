package collect.xyd.net.cn.xinyidaicollect.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2015/7/27 0027.
 */
public class MultipartFileUploader {
    public static void main(String[] args) {
        String charset = "UTF-8";
        File uploadFile1 = new File("G:/apache-tomcat-7.0.62/webapps/qianyouba_app_server/upload/IMG_20150307_130222.jpg");
        File uploadFile2 = new File("G:/apache-tomcat-7.0.62/webapps/qianyouba_app_server/upload/IMG20150211205822.jpg");
        String requestURL = "http://ceshi.xyd.net.cn/app.php/MobiApi/Index/add_che";

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

            multipart.addFormField("description", "Cool Pictures");
            multipart.addFormField("keywords", "Java,upload,Spring");

            multipart.addFilePart("fileUpload", uploadFile1);
            multipart.addFilePart("fileUpload", uploadFile2);

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
