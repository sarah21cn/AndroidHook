package com.ys.androidhook.background;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ys.androidhook.R;

/**
 * Created by yinshan on 2020/9/11.
 */
public class BackgroundFactory implements LayoutInflater.Factory2 {

  private LayoutInflater.Factory2 mViewCreateFactory2;

  @Nullable
  @Override
  public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context,
      @NonNull AttributeSet attrs) {
    return onCreateView(name, context, attrs);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull String name, @NonNull Context context,
      @NonNull AttributeSet attrs) {
    if(mViewCreateFactory2 == null){
      return null;
    }
    View view = mViewCreateFactory2.onCreateView(name, context, attrs);
    return setBackground(context, attrs, view);
  }

  private View setBackground(Context context, AttributeSet attrs, View view){
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DCustomDrawable);
    int color = typedArray.getColor(R.styleable.DCustomDrawable_d_solid_color,
        context.getResources().getColor(android.R.color.transparent));
    float radius = typedArray.getDimension(R.styleable.DCustomDrawable_d_corners_radius, 0);
    if(color != 0 || radius > 0){
      GradientDrawable drawable = new GradientDrawable();
      drawable.setColor(color);
      drawable.setCornerRadius(radius);
      view.setBackground(drawable);
    }
    typedArray.recycle();
    return view;
  }


  public void setInterceptFactory2(LayoutInflater.Factory2 factory2){
    this.mViewCreateFactory2 = factory2;
  }
}
