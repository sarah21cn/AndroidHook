package com.ys.androidhook.toast;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by yinshan on 2020/7/10.
 */
public class SafeToastContext extends ContextWrapper {

  public SafeToastContext(Context base) {
    super(base);
  }

  @Override
  public Object getSystemService(String name) {
    // 在getService时替换为WindowManagerWrapper
    if(Context.WINDOW_SERVICE.equals(name)){
      return new WindowManagerWrapper((WindowManager) getApplicationContext().getSystemService(name));
    }
    return super.getSystemService(name);
  }

  /**
   * 装饰器模式
   */
  private final class WindowManagerWrapper implements WindowManager{
    private final WindowManager base;

    public WindowManagerWrapper(WindowManager base) {
      this.base = base;
    }

    @Override
    public Display getDefaultDisplay() {
      return base.getDefaultDisplay();
    }

    @Override
    public void removeViewImmediate(View view) {
      base.removeViewImmediate(view);
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
      base.updateViewLayout(view, params);
    }

    @Override
    public void removeView(View view) {
      base.removeView(view);
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams layoutParams){
      // 在addview时增加try-catch，Android新版本的官方解法也是如此
      try{
        base.addView(view, layoutParams);
      }catch (WindowManager.BadTokenException e){
        // do nothing
      }
    }
  }
}
