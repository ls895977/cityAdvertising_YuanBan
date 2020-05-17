package com.juyijia.mm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.ugc.TXUGCBase;
import com.umeng.commonsdk.UMConfigure;
import com.juyijia.mm.http.HttpUtil;
import com.juyijia.mm.im.ImMessageUtil;
import com.juyijia.mm.im.ImPushUtil;
import com.juyijia.mm.utils.L;

import cn.tillusory.sdk.TiSDK;


/**
 * Created by cxf on 2017/8/3.
 */

public class AppContext extends MultiDexApplication {

    public static AppContext sInstance;
    public static boolean sDeBug;
    private int mCount;
    private boolean mFront;//是否前台
    //public static RefWatcher sRefWatcher;
    private boolean mBeautyInited;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sDeBug = BuildConfig.DEBUG;
//        if (sDeBug) {
//            sRefWatcher = LeakCanary.install(this);
//        }
        //初始化腾讯bugly
        CrashReport.initCrashReport(this);
        CrashReport.setAppVersion(this,AppConfig.getInstance().getVersion());
        //初始化Http
        HttpUtil.init();
        //初始化ShareSdk
//        MobSDK.init(this);
        MobSDK.init(this,"2cc15bb612fae","2dc799a378a619ad1cda03069c145e27");
        //初始化极光推送
        ImPushUtil.getInstance().init(this);
        //初始化极光IM
        ImMessageUtil.getInstance().init();
        //初始化友盟统计
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        registerActivityLifecycleCallbacks();

        //腾讯短视频（请自行替换licence）
        TXUGCBase.getInstance().setLicence(this,
                "http://license.vod2.myqcloud.com/license/v1/be376b26e8c13acac10d56308f0c549b/TXUgcSDK.licence", "0d33b836f07e4f9aae4e5af8c6cc4aeb");

    }

    /**
     * 初始化萌颜
     */
    public void initBeautySdk(String beautyKey) {
        if(AppConfig.TI_BEAUTY_ENABLE){
            if (!mBeautyInited) {
                mBeautyInited = true;
                TiSDK.init(beautyKey, this);
                L.e("萌颜初始化------->");
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mCount++;
                if (!mFront) {
                    mFront = true;
                    L.e("AppContext------->处于前台");
                    AppConfig.getInstance().setFrontGround(true);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mCount--;
                if (mCount == 0) {
                    mFront = false;
                    L.e("AppContext------->处于后台");
                    AppConfig.getInstance().setFrontGround(false);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
