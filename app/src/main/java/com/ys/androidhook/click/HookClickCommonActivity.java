package com.ys.androidhook.click;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by yinshan on 2020/7/10.
 */
public class HookClickCommonActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // 需要在View渲染完毕之后注入
    getWindow().getDecorView().post(() -> hookClick(getWindow().getDecorView()));
  }

  /**
   * 递归注入
   * @param rootView
   */
  private void hookClick(View rootView){
    HookViewClickUtils.hookView(rootView);
    if (rootView instanceof ViewGroup){
      int childCount = ((ViewGroup) rootView).getChildCount();
      for(int i = 0; i < childCount; i++){
        View child = ((ViewGroup) rootView).getChildAt(i);
        hookClick(child);
      }
    }
  }
}
