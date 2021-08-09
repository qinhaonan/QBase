package com.qhn.mybase

import android.annotation.SuppressLint
import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV

@SuppressLint("Registered")
open class BaseApplication : Application() {
    companion object {
        lateinit var application: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        MMKV.initialize(this);
//        EventBus.builder().addIndex(MyIndex()).installDefaultEventBus()
        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(AndroidLogAdapter())
            ARouter.openDebug()
        }
        ARouter.init(this)
    }
}
