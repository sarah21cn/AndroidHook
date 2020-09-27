package com.ys.androidhook.background;

import java.lang.reflect.Constructor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;

import com.ys.androidhook.R;

/**
 * Created by yinshan on 2020/9/11.
 */
public class BackgroundFactory implements LayoutInflater.Factory2 {

  private static final Class<?>[] sConstructorSignature = new Class<?>[]{Context.class, AttributeSet.class};
  private static final SimpleArrayMap<String, Constructor<? extends View>> sConstructorMap = new SimpleArrayMap<>();
  private final Object[] mConstructorArgs = new Object[2];

  private static final String[] sClassPrefixList = {
      "android.widget.",
      "android.view.",
      "android.webkit."
  };

  private LayoutInflater.Factory mViewCreateFactory;
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
    View view = null;
    if(mViewCreateFactory2 != null){
      view = mViewCreateFactory2.onCreateView(name, context, attrs);
      if(view == null){
        view = mViewCreateFactory2.onCreateView(null, name, context, attrs);
      }
    }else if(mViewCreateFactory != null){
      view = mViewCreateFactory.onCreateView(name, context, attrs);
    }
    return setBackground(name, context, attrs, view);
  }

  private View setBackground(String name, Context context, AttributeSet attrs, View view){
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DCustomDrawable);
    if(typedArray.getIndexCount() == 0){
      return view;
    }
    if(view == null){
      view = createViewFromTag(context, name, attrs);
    }
    if(view == null){
      return view;
    }
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

  public void setInterceptFactory(LayoutInflater.Factory factory){
    this.mViewCreateFactory = factory;
  }

  public void setInterceptFactory2(LayoutInflater.Factory2 factory2){
    this.mViewCreateFactory2 = factory2;
  }

  /**
   * 从AppCompatActivity中借鉴而来
   * @param context
   * @param name
   * @param attrs
   * @return
   */
  private View createViewFromTag(Context context, String name, AttributeSet attrs){
    if(name.equals("view")){
      name = attrs.getAttributeValue(null, "class");
    }

    try{
      mConstructorArgs[0] = context;
      mConstructorArgs[1] = attrs;

      if(-1 == name.indexOf(".")){
       for(int i = 0; i < sClassPrefixList.length; i++){
         final View view = createViewByPrefix(context, name, sClassPrefixList[i]);
         if(view != null){
           return view;
         }
       }
       return null;
      }else{
        return createViewByPrefix(context, name, null);
      }
    }catch (Exception e){
      return null;
    }finally {
      mConstructorArgs[0] = null;
      mConstructorArgs[1] = null;
    }
  }

  private View createViewByPrefix(Context context, String name, String prefix){
    Constructor<? extends View> constructor = sConstructorMap.get(name);
    try{
      if(constructor == null){
        Class<? extends View> clazz = Class.forName(prefix != null ? (prefix + name) : name,
            false, context.getClassLoader()).asSubclass(View.class);
        constructor = clazz.getConstructor(sConstructorSignature);
        sConstructorMap.put(name, constructor);
      }
      constructor.setAccessible(true);
      return constructor.newInstance(mConstructorArgs);
    }catch (Exception e){
      return null;
    }
  }
}
