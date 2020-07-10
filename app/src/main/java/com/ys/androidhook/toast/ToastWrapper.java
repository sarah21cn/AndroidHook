package com.ys.androidhook.toast;

import java.lang.reflect.Field;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

/**
 * Created by yinshan on 2020/7/10.
 *
 * 装饰器模式
 */
public class ToastWrapper extends Toast {

  private Toast toast;

  public ToastWrapper(Context context, Toast toast) {
    super(context);
    this.toast = toast;
  }

  public static ToastWrapper makeToast(Context context, CharSequence text, int duration){
    Toast toast = makeToast(context, text, duration);
    setContextWrapper(toast.getView(), new SafeToastContext(context));
    return new ToastWrapper(context, toast);
  }

  public static ToastWrapper makeToast(Context context, int resId, int duration){
    return makeToast(context, context.getString(resId), duration);
  }

  @Override
  public void show() {
    toast.show();
  }

  @Override
  public void setDuration(int duration) {
    toast.setDuration(duration);
  }


  @Override
  public void setGravity(int gravity, int xOffset, int yOffset) {
    toast.setGravity(gravity, xOffset, yOffset);
  }


  @Override
  public void setMargin(float horizontalMargin, float verticalMargin) {
    toast.setMargin(horizontalMargin, verticalMargin);
  }


  @Override
  public void setText(int resId) {
    toast.setText(resId);
  }


  @Override
  public void setText(CharSequence s) {
    toast.setText(s);
  }


  @Override
  public void setView(View view) {
    toast.setView(view);
    setContextWrapper(view, new SafeToastContext(view.getContext()));
  }


  @Override
  public float getHorizontalMargin() {
    return toast.getHorizontalMargin();
  }


  @Override
  public float getVerticalMargin() {
    return toast.getVerticalMargin();
  }


  @Override
  public int getDuration() {
    return toast.getDuration();
  }


  @Override
  public int getGravity() {
    return toast.getGravity();
  }


  @Override
  public int getXOffset() {
    return toast.getXOffset();
  }


  @Override
  public int getYOffset() {
    return toast.getYOffset();
  }


  @Override
  public View getView() {
    return toast.getView();
  }


  private static void setContextWrapper(View view, Context context){
    if (Build.VERSION.SDK_INT == 25){
      try{
        Field field = View.class.getDeclaredField("mContext");
        field.setAccessible(true);
        field.set(view, context);
      }catch (Throwable throwable){
      }
    }
  }
}
