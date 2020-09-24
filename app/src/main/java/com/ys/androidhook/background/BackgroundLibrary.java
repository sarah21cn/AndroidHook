package com.ys.androidhook.background;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.LayoutInflaterCompat;

/**
 * Created by yinshan on 2020/9/23.
 */
public class BackgroundLibrary {

  public static LayoutInflater inject(Context context) {
    LayoutInflater inflater;
    if (context instanceof Activity) {
      inflater = ((Activity) context).getLayoutInflater();
    } else {
      inflater = LayoutInflater.from(context);
    }
    if (inflater == null) {
      return null;
    }
    if (inflater.getFactory2() == null) {
      BackgroundFactory factory = setDelegateFactory(context);
      inflater.setFactory2(factory);
    } else if (!(inflater.getFactory2() instanceof BackgroundFactory)) {
      forceSetFactory2(inflater);
    }
    return inflater;
  }

  public static BackgroundFactory setDelegateFactory(Context context) {
    BackgroundFactory factory = new BackgroundFactory();
    if (context instanceof AppCompatActivity) {
      final AppCompatDelegate delegate = ((AppCompatActivity) context).getDelegate();
      factory.setInterceptFactory2(new LayoutInflater.Factory2() {
        @Nullable
        @Override
        public View onCreateView(@Nullable View parent, @NonNull String name,
            @NonNull Context context, @NonNull AttributeSet attrs) {
          return delegate.createView(parent, name, context, attrs);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull String name, @NonNull Context context,
            @NonNull AttributeSet attrs) {
          return delegate.createView(null, name, context, attrs);
        }
      });
    }
    return factory;
  }

  private static void forceSetFactory2(LayoutInflater inflater) {
    Class<LayoutInflaterCompat> compatClass = LayoutInflaterCompat.class;
    Class<LayoutInflater> inflaterClass = LayoutInflater.class;
    try {
      Field sCheckedField = compatClass.getDeclaredField("sCheckedField");
      sCheckedField.setAccessible(true);
      sCheckedField.setBoolean(inflater, false);
      Field mFactory = inflaterClass.getDeclaredField("mFactory");
      mFactory.setAccessible(true);
      Field mFactory2 = inflaterClass.getDeclaredField("mFactory2");
      mFactory2.setAccessible(true);
      BackgroundFactory factory = new BackgroundFactory();
      if (inflater.getFactory2() != null) {
        factory.setInterceptFactory2(inflater.getFactory2());
      }
      mFactory2.set(inflater, factory);
      mFactory.set(inflater, factory);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }
}
