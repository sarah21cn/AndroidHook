package com.ys.androidhook.skin;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;

/**
 * Created by yinshan on 2020/9/27.
 */
public class SkinManager {

  private Context context;
  private List<ISkinUpdateListener> skinUpdateListeners;

  private static class Holder{
    private static SkinManager sManager = new SkinManager();
  }

  public static SkinManager getInstance(){
    return Holder.sManager;
  }

  private SkinManager(){

  }

  /**
   * 使用Application Context
   * @param context
   */
  public void init(Context context){
    context = context.getApplicationContext();
  }

  public void attach(ISkinUpdateListener listener){
    if(skinUpdateListeners == null){
      skinUpdateListeners = new ArrayList<>();
    }
    if(!skinUpdateListeners.contains(listener)){
      skinUpdateListeners.add(listener);
    }
  }

  public void detach(ISkinUpdateListener listener){
    if(skinUpdateListeners == null){
      return;
    }
    if(skinUpdateListeners.contains(listener)){
      skinUpdateListeners.remove(listener);
    }
  }

  private void notifySkinUpdate(){
    if(skinUpdateListeners == null){
      return;
    }
    for(ISkinUpdateListener listener : skinUpdateListeners){
      listener.onThemeUpdate();
    }
  }

  @SuppressLint("StaticFieldLeak")
  public void load(String skinPkgPath){
    new AsyncTask<String, Void, Resources>(){
      @Override
      protected void onPreExecute() {
        super.onPreExecute();
      }

      @Override
      protected Resources doInBackground(String... strings) {
        try{
          if(strings.length == 1){
            String skinPkgPath = strings[0];

            File file = new File(skinPkgPath);
            if(file == null || !file.exists()){
              return null;
            }

            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, skinPkgPath);

            Resources superRes = context.getResources();
            Resources skinResource = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());

            return skinResource;
          }
        }catch (Exception e){
          e.printStackTrace();
        }
        return null;
      }

      @Override
      protected void onPostExecute(Resources resources) {
        super.onPostExecute(resources);
      }
    }.execute(skinPkgPath);
  }

}
