package collect.xyd.net.cn.xinyidaicollect.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import collect.xyd.net.cn.xinyidaicollect.entity.BusinessImage;
import collect.xyd.net.cn.xinyidaicollect.service.BusinessInfo;
import collect.xyd.net.cn.xinyidaicollect.entity.BusinessVideo;
import collect.xyd.net.cn.xinyidaicollect.utils.L;
import collect.xyd.net.cn.xinyidaicollect.utils.SPUtils;
import collect.xyd.net.cn.xinyidaicollect.utils.T;

/**
 * @FileName:collect.xyd.net.cn.xinyidaicollect.db.XinyidaiDB.java
 * @author:Phoenix
 * @date:2015-09-14 10:13
 * @Version:V1.0
 */
public class XinyidaiDB {
    private static final String TAG = "XinyidaiDB";
    public static final String DB_NAME = "xinyidai.db";
    private static final String VERSION = "version";
    private static final String TABLE_AGENTS = "agents";
    private static final String TABLE_AGENTS_IMG = "agent_img";
    private static final String TABLE_AGENTS_VIDEO = "agent_video";
    private static final String TABLE_CHE = "che";
    private static final String TABLE_CHE_IMG = "che_img";
    private static final String TABLE_CHE_VIDEO = "che_video";
    private static final String TABLE_CHE_audio = "che_audio";
    private static final String TABLE_FANG = "fang";
    private static final String TABLE_FANG_IMG = "fang_img";
    private static final String TABLE_FANG_VIDEO = "fang_video";
    private SQLiteDatabase sqlDB;
    private static volatile XinyidaiDB xydDB = null;

    private XinyidaiDB(Context context) {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + "cn.net.xinyidai" + File.separator
                + DB_NAME;
        File db = new File(path);
        if (!db.exists() || (Integer) SPUtils.get(context, VERSION, -1) < 0) {
            L.i("db is not exists");
            try {
                InputStream is = context.getAssets().open(DB_NAME);
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
                SPUtils.put(context, VERSION, 1);// 用于管理数据库版本，如果数据库有重大更新时使用
            } catch (IOException e) {
                e.printStackTrace();
                T.showLong(context, e.getMessage());
                System.exit(0);
            }
        }
        sqlDB = context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
    }

    public static XinyidaiDB getInstance(Context context) {
        if (xydDB == null) {
            synchronized (XinyidaiDB.class) {
                if (xydDB == null) {
                    return new XinyidaiDB(context);
                }
            }
        }
        return xydDB;
    }

    public boolean isOpen() {
        return sqlDB != null && sqlDB.isOpen();
    }

    public void close() {
        if (sqlDB != null && sqlDB.isOpen())
            sqlDB.close();
    }

    /**
     * 插入商家信息
     * @param info
     * @return
     */
    public boolean insertBusinessInfo(BusinessInfo info) {
        boolean insertFlag = true;
        try {
            sqlDB.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put("user_id", info.getUser_id());
            cv.put("username", info.getUsername());
            cv.put("password", info.getPassword());
            cv.put("agent_naem", info.getAgent_name());
            cv.put("type", info.getType());
            cv.put("status", info.getStatus());
            cv.put("linkman", info.getLink_man());
            cv.put("tel", info.getTel());
            cv.put("mobile", info.getMobile());
            cv.put("description", info.getDescription());
            cv.put("address", info.getAddress());
            cv.put("gps_y", info.getGps_y());
            cv.put("gps_x", info.getGps_x());
            long agentId = sqlDB.insert(TABLE_AGENTS, null, cv);
            if (agentId > 0) {
                long imgId;
                for (BusinessImage image : info.getImages()) {
                    ContentValues imgCV = new ContentValues(2);
                    imgCV.put("agent_id", agentId);
                    imgCV.put("img_path", image.getImg_path());
                    imgId = sqlDB.insert(TABLE_AGENTS_IMG, null, imgCV);
                    if (imgId <= 0) {
                        insertFlag = false;
                    }
                }
                BusinessVideo inside_video = info.getVideo().get("inside_video");
                ContentValues videoIn = new ContentValues(3);
                videoIn.put("agent_id", agentId);
                videoIn.put("img_path", inside_video.getVideo_path());
                videoIn.put("type", 1);
                long videoInId = sqlDB.insert(TABLE_AGENTS_VIDEO, null, videoIn);
                if (videoInId <= 0) {
                    insertFlag = false;
                }
                BusinessVideo outside_video = info.getVideo().get("outside_video");
                ContentValues videoOut = new ContentValues(3);
                videoOut.put("agent_id", agentId);
                videoOut.put("img_path", outside_video.getVideo_path());
                videoOut.put("type", 2);
                long videoOutId = sqlDB.insert(TABLE_AGENTS_VIDEO, null, videoIn);
                if (videoOutId <= 0) {
                    insertFlag = false;
                }
            }else{
                insertFlag = false;
            }
            sqlDB.setTransactionSuccessful();
        }finally {
            sqlDB.endTransaction();
        }
        return insertFlag;
    }

    /**
     * 根据id获取商家信息
     * @param agent_id
     * @return
     */
    public BusinessInfo getBusinessInfo(int agent_id) {
        BusinessInfo info = null;
        String[] selection = new String[]{"agent_id", "user_id", "username", "password", "agent_name"
                , "type", "status", "linkman", "tel", "mobile", "description", "address", "gps_y", "gps_x"};
        sqlDB.beginTransaction();
        Cursor c, cImg, cVideo;
        try {
            c = sqlDB.query(TABLE_AGENTS, selection, "agent_id=?", new String[]{agent_id + ""}, null, null, null);
            cImg = sqlDB.query(TABLE_AGENTS_IMG, new String[]{"img_id", "img_path"}, "agent_id=?", new String[]{agent_id + ""}, null, null, null);
            cVideo = sqlDB.query(TABLE_AGENTS_VIDEO, new String[]{"video_id", "video_path", "type"}, "agent_id=?", new String[]{agent_id + ""}, null, null, null);
            sqlDB.setTransactionSuccessful();
        } finally {
            sqlDB.endTransaction();
        }
        if (c!=null && c.moveToFirst()) {
            int userId = c.getInt(c.getColumnIndex("user_id"));
            String username = c.getString(c.getColumnIndex("username"));
            String password = c.getString(c.getColumnIndex("password"));
            String agentName = c.getString(c.getColumnIndex("agent_name"));
            int type = c.getInt(c.getColumnIndex("type"));
            int status = c.getInt(c.getColumnIndex("status"));
            String linkMan = c.getString(c.getColumnIndex("linkman"));
            String tel = c.getString(c.getColumnIndex("tel"));
            String mobile = c.getString(c.getColumnIndex("mobile"));
            String description = c.getString(c.getColumnIndex("description"));
            String address = c.getString(c.getColumnIndex("address"));
            String gps_y = c.getString(c.getColumnIndex("gps_y"));
            String gps_x = c.getString(c.getColumnIndex("gps_x"));
            info = new BusinessInfo(agent_id, userId, username, password, agentName,
                    type, status, linkMan, tel, mobile, description,
                    address, Double.valueOf(gps_y), Double.valueOf(gps_x));
        }
        List<BusinessImage> images = new ArrayList<BusinessImage>(4);
        while (cImg!=null && cImg.moveToNext()) {
            int img_id = cImg.getInt(cImg.getColumnIndex("img_id"));
            String img_path = cImg.getString(cImg.getColumnIndex("img_path"));
            BusinessImage image = new BusinessImage();
            image.setImg_id(img_id);
            image.setAgent_id(agent_id);
            image.setImg_path(img_path);
            images.add(image);
        }
       if(info!=null)info.setImages(images);
        Map<String, BusinessVideo> videoMap = new HashMap<String, BusinessVideo>(2);
        while (cImg!=null && cVideo.moveToNext()) {
            int video_id = cVideo.getInt(cVideo.getColumnIndex("video_id"));
            String video_path = cVideo.getString(cVideo.getColumnIndex("video_path"));
            int type = cVideo.getInt(cVideo.getColumnIndex("type"));
            BusinessVideo video = new BusinessVideo();
            video.setVideo_id(video_id);
            video.setVideo_path(video_path);
            video.setAgent_id(agent_id);
            video.setType(type);
            if (type == 1) {
                videoMap.put("video_inside", video);
            } else if (type == 2) {
                videoMap.put("vidwo_outside", video);
            }
        }
        if(info!=null)info.setVideo(videoMap);
        return info;
    }

    /**
     * 获取所有商家信息
     * @return
     */
    public List<BusinessInfo> getBusinessInfos() {
        List<BusinessInfo> infos = new ArrayList<BusinessInfo>();
        Cursor c = sqlDB.query(TABLE_AGENTS, new String[]{"agent_id", "agent_name", "linkman", "mobile", "address"}, null, null, null, null, null);
        while (c.moveToNext()) {
            int agentId = c.getInt(c.getColumnIndex("agent_id"));
            String agentName = c.getString(c.getColumnIndex("agent_name"));
            String linkMan = c.getString(c.getColumnIndex("linkman"));
            String mobile = c.getString(c.getColumnIndex("mobile"));
            String address = c.getString(c.getColumnIndex("address"));
            BusinessInfo info = new BusinessInfo(agentId, agentName, linkMan, mobile, address);
            infos.add(info);
        }
        return infos;
    }

    /**
     * 删除商家信息
     * @param ids
     * @return
     */
    public boolean deleteBusinessInfos(int[] ids) {
        int len = ids.length;
        if (len > 0) {
            StringBuffer sql_agents = new StringBuffer("delete from " + TABLE_AGENTS + "where agent_id in (");
            StringBuffer sql_imgs = new StringBuffer("delete from " + TABLE_AGENTS_IMG + " where agent_id in (");
            StringBuffer sql_videos = new StringBuffer("delete from " + TABLE_AGENTS_VIDEO + "where agent_id in (");
            for (int i = 0; i < len; i++) {
                sql_agents.append(ids[i]);
                sql_imgs.append(ids[i]);
                sql_videos.append(ids[i]);
                if (i < len - 1) {
                    sql_agents.append(",");
                    sql_imgs.append(",");
                    sql_videos.append(",");
                }
            }
            sql_agents.append(")");
            sql_imgs.append(")");
            sql_videos.append(")");
            try {
                sqlDB.beginTransaction();
                sqlDB.execSQL(sql_agents.toString());
                sqlDB.execSQL(sql_imgs.toString());
                sqlDB.execSQL(sql_videos.toString());
                sqlDB.setTransactionSuccessful();
            }finally {
                sqlDB.endTransaction();
            }
            return true;
        }
        return false;
    }

}
