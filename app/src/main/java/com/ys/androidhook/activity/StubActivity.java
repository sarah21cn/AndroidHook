package com.ys.androidhook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.ys.androidhook.R;

/**
 * Created by yinshan on 2020/8/11.
 */
public class StubActivity extends Activity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stub);
    TextView textView = findViewById(R.id.text_view);
    textView.setText("Stub");
  }
}
