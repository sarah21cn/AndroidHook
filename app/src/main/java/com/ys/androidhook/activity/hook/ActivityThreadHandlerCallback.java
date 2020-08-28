package com.ys.androidhook.activity.hook;

import java.util.List;

import android.app.ActivityThread;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;

import com.ys.androidhook.utils.Reflector;

/**
 * Created by yinshan on 2020/8/11.
 */
public class ActivityThreadHandlerCallback implements Handler.Callback {

  private static final String TAG = "HandlerCallback";

  private final int EXECUTE_TRANSACTION = 159;

  private int what = 0;

  Handler mBase;


  public ActivityThreadHandlerCallback(Handler base) {
    mBase = base;
  }

  @Override
  public boolean handleMessage(@NonNull Message msg) {
    Log.d(TAG, "handle Message " + msg.what);
    if(what == 0){
      try{
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        Handler handler = Reflector.with(activityThread).field("mH").get();
        what = Reflector.with(handler).field("EXECUTE_TRANSACTION").get();
      }catch (Reflector.ReflectedException e){
        e.printStackTrace();
        what = EXECUTE_TRANSACTION;
      }
    }
    if(msg.what == what){
      handleLaunchActivity(msg);
    }
    return false;
  }

  private void handleLaunchActivity(Message msg){
    try{
      List list = Reflector.with(msg.obj).field("mActivityCallbacks").get();
      if(list == null || list.isEmpty()) return;
      Class<?> launchActivityItemClz = Class.forName("android.app.servertransaction.LaunchActivityItem");
      if(launchActivityItemClz.isInstance(list.get(0))) {
        Intent intent = Reflector.with(list.get(0)).field("mIntent").get();
        Intent target = intent.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
        intent.setComponent(target.getComponent());
      }
    }catch (Reflector.ReflectedException e){
      e.printStackTrace();
    }catch (ClassNotFoundException e){
      e.printStackTrace();
    }
  }
}
