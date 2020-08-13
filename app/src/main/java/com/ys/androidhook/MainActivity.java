package com.ys.androidhook;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ys.androidhook.activity.StubActivity;
import com.ys.androidhook.activity.TargetActivity;
import com.ys.androidhook.activity.hook.AMSHookHelper;
import com.ys.androidhook.click.HookClickCommonActivity;
import com.ys.androidhook.toast.ToastWrapper;

public class MainActivity extends HookClickCommonActivity{

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(newBase);

    try{
      if(Build.VERSION.SDK_INT >= 29){
        AMSHookHelper.hookActivityManagerNativeSDK29();
      }else{
        AMSHookHelper.hookActivityManagerNative();
      }
      AMSHookHelper.hookActivityThreadCallback();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.click_btn).setOnClickListener(
        v -> ToastWrapper.makeText(MainActivity.this, "点击按钮", Toast.LENGTH_SHORT).show());

    findViewById(R.id.activity_btn).setOnClickListener(v -> {
      try{
        Intent intent = new Intent(MainActivity.this, TargetActivity.class);
        startActivity(intent);
      }catch (ActivityNotFoundException e){
        e.printStackTrace();
      }
    });
  }
}
