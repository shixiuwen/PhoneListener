package com.example.shixiuwen.phonelistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by shixiuwen on 15-10-16.
 */
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("我接受到了一个广播，让我开启服务监听手机状态");

        intent = new Intent(context,PhoneListenerService.class);

        //接收到广播后开启服务
        context.startService(intent);
    }
}
