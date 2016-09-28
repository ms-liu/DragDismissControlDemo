package com.demo.ms.dragdismisscontroldemo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * ==============================================
 * 类名：
 *
 * 作者：M-Liu
 *
 * 时间：2016/9/27
 *
 * 邮箱：ms_liu163@163.com
 * ==============================================
 */

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
