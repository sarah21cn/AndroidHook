package com.ys.androidhook.activity.hook;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.ys.androidhook.activity.StubActivity;

/**
 * Created by yinshan on 2020/8/11.
 */
public class IActivityManagerHandler implements InvocationHandler {

  private static final String TAG = "IActivityManagerHandler";

  Object mBase;

  public IActivityManagerHandler(Object base) {
    mBase = base;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    Log.d(TAG, "invoke " + method.getName());
    // 如果是启动Activity，替换Intent
    if("startActivity".equals(method.getName())){
      Intent raw;
      int index = 0;
      for(int i = 0; i < args.length; i++){
        if(args[i] instanceof Intent){
          index = i;
          break;
        }
      }
      raw = (Intent) args[index];

      Intent newIntent = new Intent();
      String stubPackage = "com.ys.androidhook";
      ComponentName componentName = new ComponentName(stubPackage, StubActivity.class.getName());
      newIntent.setComponent(componentName);

      // 将之前的intent存起来
      newIntent.putExtra(AMSHookHelper.EXTRA_TARGET_INTENT, raw);
      args[index] = newIntent;
      Log.d(TAG, "hook succeed");
      return method.invoke(mBase, args);
    }

    return method.invoke(mBase, args);
  }
}
