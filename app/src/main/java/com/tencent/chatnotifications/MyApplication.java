package com.tencent.chatnotifications;

import android.app.Application;

import com.socks.library.KLog;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(true, "MyApplication");


    }

}
