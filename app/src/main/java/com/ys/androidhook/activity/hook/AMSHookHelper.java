package com.ys.androidhook.activity.hook;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.ReflectPermission;

import android.app.ActivityThread;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.ArrayMap;

import com.ys.androidhook.utils.Reflector;

/**
 * Created by yinshan on 2020/8/11.
 */
public class AMSHookHelper {

  public static final String EXTRA_TARGET_INTENT = "extra_target_intent";

  @Deprecated
  private static void hookActivityManagerNativeSDK29() throws ClassNotFoundException, Reflector.ReflectedException {
    Object singletonObj = Reflector.on("android.app.ActivityTaskManager").field("IActivityTaskManagerSingleton").get();
    Class<?> iActivityTaskManagerInterface = Class.forName("android.app.IActivityTaskManager");
    Object rawIActivityTaskManager = Reflector.with(singletonObj).field("mInstance").get();
    Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class<?>[] { iActivityTaskManagerInterface }, new IActivityManagerHandler(rawIActivityTaskManager));
    Reflector.with(singletonObj).field("mInstance").set(proxy);
  }

  @Deprecated
  private static void hookActivityManagerNative() throws NoSuchFieldException,
      ClassNotFoundException, IllegalAccessException{
    // 获取IActivityManagerSingleton
    Field gDefaultField = null;
    if(Build.VERSION.SDK_INT >= 26){
      Class<?> activityManager = Class.forName("android.app.ActivityManager");
      gDefaultField = activityManager.getDeclaredField("IActivityManagerSingleton");
    }else{
      Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
      gDefaultField = activityManagerNative.getDeclaredField("gDefault");
    }
    gDefaultField.setAccessible(true);

    // 获取单例对象
    Object gDefault = gDefaultField.get(null);
    Class<?> singleton = Class.forName("android.util.Singleton");
    Field instanceField = singleton.getDeclaredField("mInstance");
    instanceField.setAccessible(true);

    // 为IActivityManager设置代理类
    Object rawIActivityManager = instanceField.get(gDefault);
    Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
    Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class<?>[] { iActivityManagerInterface }, new IActivityManagerHandler(rawIActivityManager));
    instanceField.set(gDefault, proxy);
  }

  public static void hookActivityManagerService() throws Reflector.ReflectedException{
    Object gDefaultObj = null;
    // API 29 及以后hook android.app.ActivityTaskManager.IActivityTaskManagerSingleton
    // API 26 及以后hook android.app.ActivityManager.IActivityManagerSingleton
    // API 25 以前hook android.app.ActivityManagerNative.gDefault
    if(Build.VERSION.SDK_INT >= 29){
      gDefaultObj = Reflector.on("android.app.ActivityTaskManager").field("IActivityTaskManagerSingleton").get();
    }else if(Build.VERSION.SDK_INT >= 26){
      gDefaultObj = Reflector.on("android.app.ActivityManager").field("IActivityManagerSingleton").get();
    }else{
      gDefaultObj = Reflector.on("android.app.ActivityManagerNative").field("gDefault").get();
    }
    Object amsObj = Reflector.with(gDefaultObj).field("mInstance").get();
    Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        amsObj.getClass().getInterfaces(), new IActivityManagerHandler(amsObj));
    Reflector.with(gDefaultObj).field("mInstance").set(proxy);
  }


  public static void hookActivityThreadCallback() throws Reflector.ReflectedException {
//    Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
//    Field currentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
//    currentActivityThreadField.setAccessible(true);
//    Object currentActivityThread = currentActivityThreadField.get(null);
//
//    Field mHField = activityThreadClass.getDeclaredField("mH");
//    mHField.setAccessible(true);
//    Handler mH = (Handler) mHField.get(currentActivityThread);

//    Field mCallbackField = Handler.class.getDeclaredField("mCallback");
//    mCallbackField.setAccessible(true);
//
//    mCallbackField.set(mH, new ActivityThreadHandlerCallback(mH));

    ActivityThread activityThread = ActivityThread.currentActivityThread();
    Handler handler = Reflector.with(activityThread).field("mH").get();
    Reflector.with(handler).field("mCallback").set(new ActivityThreadHandlerCallback(handler));
  }



}
