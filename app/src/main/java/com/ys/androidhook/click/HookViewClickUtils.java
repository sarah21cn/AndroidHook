package com.ys.androidhook.click;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.view.View;

/**
 * Created by yinshan on 2020/7/10.
 */
public class HookViewClickUtils {

  /**
   * hook view的 mClickListener
   * @param view
   */
  public static void hookView(View view){
    try{
      // 获取到getListenerInfo的返回值
      Class clazz = Class.forName("android.view.View");
      Method listenerInfoMethod = clazz.getDeclaredMethod("getListenerInfo");
      listenerInfoMethod.setAccessible(true);
      Object listenerInfoObject = listenerInfoMethod.invoke(view);

      // 获取ListenerInfo中的mOnClickListener
      Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
      Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");
      onClickListenerField.setAccessible(true);
      View.OnClickListener onClickListener = (View.OnClickListener) onClickListenerField.get(listenerInfoObject);

      // 使用自定义onClickListener替换到原生
      View.OnClickListener onClickListenerProxy = new ClickListenerProxy(onClickListener);
      onClickListenerField.set(listenerInfoObject, onClickListenerProxy);
    }catch (Exception e){
      e.printStackTrace();
    }
  }


  /**
   * 自定义onClickListener，防止多次点击
   */
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
