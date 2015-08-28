package collect.xyd.net.cn.xinyidaicollect.utils;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2015/7/31 0031.
 */
public class RecordAudio {
    private MediaRecorder mMediaRecorder;
    private File recAudioFile;
    public RecordAudio(String path,String fileName){
        File filePath = new File(path);
        if (!filePath.exists()){
            filePath.mkdirs();
        }
        recAudioFile = new File(path+fileName);
    }
    public void startRecorder() {
        mMediaRecorder = new MediaRecorder();
        if (recAudioFile.exists()) {
            recAudioFile.delete();
        }

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setMaxDuration(30000);
        mMediaRecorder.setOutputFile(recAudioFile.getAbsolutePath());
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecorder(){
        if (recAudioFile!=null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
        }
    }

}
