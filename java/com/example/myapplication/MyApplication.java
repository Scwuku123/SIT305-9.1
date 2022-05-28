package com.example.myapplication;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.example.myapplication.Utils.SPUtils;


public class MyApplication extends Application {

    public static MyApplication application;
    private static Context context;

    /**
     * 获取上下文
     * @return
     */
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = getApplicationContext();
        SPUtils.init(this);
        //第一：默认初始化
    }

}
