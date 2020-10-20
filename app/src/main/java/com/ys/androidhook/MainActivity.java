package com.ys.androidhook;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ys.androidhook.activity.StubActivity;
import com.ys.androidhook.activity.TargetActivity;
import com.ys.androidhook.activity.hook.AMSHookHelper;
import com.ys.androidhook.background.ViewBackgroundActivity;
import com.ys.androidhook.click.HookClickCommonActivity;
import com.ys.androidhook.pkg.PackageActivity;
import com.ys.androidhook.toast.ToastWrapper;

public class MainActivity extends HookClickCommonActivity{

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(newBase);

    try{
      AMSHookHelper.hookActivityManagerService();
      AMSHookHelper.hookActivityThreadCallback();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onClick(View view){
    switch (view.getId()){
      case R.id.click_btn:
        ToastWrapper.makeText(this, "点击按钮", Toast.LENGTH_SHORT).show();
        break;
      case R.id.activity_btn:
        try{
          // 不在Manifest中注册TargetActivity，启动
          Intent intent = new Intent(this, TargetActivity.class);
          startActivity(intent);
        }catch (ActivityNotFoundException e){
          e.printStackTrace();
        }
        break;
      case R.id.background_btn:
        Intent intent = new Intent(this, ViewBackgroundActivity.class);
        startActivity(intent);
        break;
      case R.id.package_btn:
        intent = new Intent(this, PackageActivity.class);
        startActivity(intent);
        break;
    }
  }
}
