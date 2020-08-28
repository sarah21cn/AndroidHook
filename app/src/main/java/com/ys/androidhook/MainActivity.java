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
import com.ys.androidhook.click.HookClickCommonActivity;
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

    findViewById(R.id.click_btn).setOnClickListener(
        v -> {
          ToastWrapper.makeText(MainActivity.this, "点击按钮", Toast.LENGTH_SHORT).show();
//            Uri uri = Uri.parse("kwai://tag/topic/荒野乱斗");
//            Uri uri = Uri.parse("main://ys");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
        });

    findViewById(R.id.activity_btn).setOnClickListener(v -> {
      try{
        // 不在Manifest中注册TargetActivity，启动
        Intent intent = new Intent(MainActivity.this, TargetActivity.class);
        startActivity(intent);
      }catch (ActivityNotFoundException e){
        e.printStackTrace();
      }
//
//      Uri uri = Uri.parse("kwai://work/31225174827?coverAspectRatio=0.5625&coverUrl=https%3A%2F%2Ftx2.a.yximgs.com%2Fupic%2F2020%2F06%2F26%2F13%2FBMjAyMDA2MjYxMzIwMDlfMTg2MjU4NTc1MF8zMTIyNTE3NDgyN18xXzM%3D_low_Bee6b980e5ea4903a1092f477c2848b76.webp%3Fdi%3D676bd8e6%26bp%3D10001");
//      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//      startActivity(intent);
    });
  }
}
