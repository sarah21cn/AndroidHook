package com.ys.androidhook.activity.hook;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;

/**
 * Created by yinshan on 2020/8/11.
 */
public class ActivityThreadHandlerCallback implements Handler.Callback {

  private static final String TAG = "HandlerCallback";

  Handler mBase;

  public ActivityThreadHandlerCallback(Handler base) {
    mBase = base;
  }

  @Override
  public boolean handleMessage(@NonNull Message msg) {
    Log.d(TAG, "handle Message " + msg.what);
    switch (msg.what){
      case 159:
        handleLaunchActivity(msg);
        break;
    }
    return false;
  }



  private void handleLaunchActivity(Message msg){
    Object obj = msg.obj;
    try{
      Field intent = obj.getClass().getDeclaredField("intent");
      intent.setAccessible(true);
      Intent raw = (Intent) intent.get(obj);

      Intent target = raw.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
      raw.setComponent(target.getComponent());

      Log.d(TAG, "hook succeed");
    }catch (NoSuchFieldException e){
      e.printStackTrace();
    }catch (IllegalAccessException e){
      e.printStackTrace();
    }
  }
}
