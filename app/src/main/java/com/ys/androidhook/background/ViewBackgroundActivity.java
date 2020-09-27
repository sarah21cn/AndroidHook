package com.ys.androidhook.background;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ys.androidhook.R;

/**
 * Created by yinshan on 2020/9/11.
 */
public class ViewBackgroundActivity extends Activity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    BackgroundLibrary.inject(this);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_background);
  }
}
