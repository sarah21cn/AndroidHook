package com.ys.androidhook.activity.hook;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.ArrayMap;

/**
 * Created by yinshan on 2020/8/11.
 */
public class AMSHookHelper {

  public static final String EXTRA_TARGET_INTENT = "extra_target_intent";

  public static void hookActivityManagerNativeSDK29() throws NoSuchFieldException,
      ClassNotFoundException, IllegalAccessException{

    Class<?> activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
    Field gDefaultField = activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
    gDefaultField.setAccessible(true);

    // 获取单例对象
    Object gDefault = gDefaultField.get(null);
    Class<?> singleton = Class.forName("android.util.Singleton");
    Field instanceField = singleton.getDeclaredField("mInstance");
    instanceField.setAccessible(true);

    // 为IActivityManager设置代理类
    Object rawIActivityTaskManager = instanceField.get(gDefault);
    Class<?> iActivityTaskManagerInterface = Class.forName("android.app.IActivityTaskManager");
    Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class<?>[] { iActivityTaskManagerInterface }, new IActivityManagerHandler(rawIActivityTaskManager));
    instanceField.set(gDefault, proxy);
  }

  public static void hookActivityManagerNative() throws NoSuchFieldException,
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


  public static void hookActivityThreadCallback() throws NoSuchFieldException,
      ClassNotFoundException, IllegalAccessException{
    Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
    Field currentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
    currentActivityThreadField.setAccessible(true);
    Object currentActivityThread = currentActivityThreadField.get(null);

    Field mHField = activityThreadClass.getDeclaredField("mH");
    mHField.setAccessible(true);
    Handler mH = (Handler) mHField.get(currentActivityThread);

    Field mCallbackField = Handler.class.getDeclaredField("mCallback");
    mCallbackField.setAccessible(true);

    mCallbackField.set(mH, new ActivityThreadHandlerCallback(mH));
  }

  public static void hookActivityClientRecord(IBinder token) throws NoSuchFieldException,
      ClassNotFoundException, IllegalAccessException{
    Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
    Field currentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
    currentActivityThreadField.setAccessible(true);
    Object currentActivityThread = currentActivityThreadField.get(null);

    Field mActivitiesField = activityThreadClass.getDeclaredField("mActivities");
    mActivitiesField.setAccessible(true);
    // TODO: 2020/8/13 替换mActivities中的StubActivity
  }

}
