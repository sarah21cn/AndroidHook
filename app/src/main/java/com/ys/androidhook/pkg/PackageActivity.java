package com.ys.androidhook.pkg;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ys.androidhook.R;
import com.ys.androidhook.utils.Reflector;

/**
 * Created by yinshan on 2020/10/20.
 */
public class PackageActivity extends AppCompatActivity {

  private static final String TAG = "PackageActivity";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_package);
  }

  public void onClick(View view){
    switch (view.getId()){
      case R.id.load_btn:
        final String path = getFilesDir().getPath() + "/plugindemo-debug.apk";
        File file = new File(path);
        if(file.exists()){
          try{
            Object object = Reflector.on("android.content.pm.PackageParser").constructor().newInstance();
            Object packageObj = Reflector.with(object).method("parsePackage", File.class, int.class).call(file, 4);
            if(packageObj != null){
              Log.d(TAG, "parser succeed");
            }
          }catch (Exception e){
            e.printStackTrace();
          }
        }else{
          Log.d(TAG, "file not exists");
        }
        break;
    }
  }

}
