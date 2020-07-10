package com.ys.androidhook.click;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.view.View;

/**
 * Created by yinshan on 2020/7/10.
 */
public class HookViewClickUtils {

  public static void hookView(View view){
    try{
      Method listenerInfoMethod = View.class.getMethod("getListenerInfo");
      listenerInfoMethod.setAccessible(true);
      Object listenerInfoObject = listenerInfoMethod.invoke(view);

      Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
      Field onClickListenerField = listenerInfoClazz.getField("mOnClickListener");
      onClickListenerField.setAccessible(true);

      View.OnClickListener onClickListener = (View.OnClickListener) onClickListenerField.get(listenerInfoObject);
      View.OnClickListener onClickListenerProxy = new ClickListenerProxy(onClickListener);
      onClickListenerField.set(listenerInfoObject, onClickListenerProxy);
    }catch (Exception e){
      e.printStackTrace();
    }
  }


  private static class ClickListenerProxy implements View.OnClickListener{

    private static final long CLICK_DELAY_TIME = 1000;

    private long lastClickTime = 0;

    private View.OnClickListener clickListener;

    public ClickListenerProxy(View.OnClickListener clickListener) {
      this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
      if(System.currentTimeMillis() - lastClickTime > CLICK_DELAY_TIME){
        lastClickTime = System.currentTimeMillis();
        if(clickListener != null){
          clickListener.onClick(v);
        }
      }
    }
  }
}
