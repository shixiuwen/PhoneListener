package com.example.shixiuwen.phonelistener;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by shixiuwen on 15-10-16.
 */
public class PhoneListenerService extends Service {

    private TelephonyManager tm;
    private Mylistener myListener;
    private MediaRecorder mediaRecorder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("服务被创建了");
        tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        myListener = new Mylistener();
        tm.listen(myListener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        //当服务销毁的时候不再监听
        tm.listen(myListener,PhoneStateListener.LISTEN_NONE);
        myListener = null;
        super.onDestroy();
    }

    private class Mylistener extends PhoneStateListener{

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:  //空闲状态
                    if(mediaRecorder!=null){
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        System.out.println("录制完毕，开始上传文件到服务器……");
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: //通话状态
                    //1.初始化一个录音机
                    mediaRecorder = new MediaRecorder();
                    //2.设置声音源
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                    //3.设置录制文件输出格式
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    //4.指定录音文件的名称
                    File file = new File(Environment.getRootDirectory(),System.currentTimeMillis()+".3gp");
                    mediaRecorder.setOutputFile(file.getAbsolutePath());
                    //5.设置音频的编码
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    //6.准备录音
                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //7.开始录音
                    mediaRecorder.start();
                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃状态

                    break;
                default:
                    break;
            }
        }
    }
}
