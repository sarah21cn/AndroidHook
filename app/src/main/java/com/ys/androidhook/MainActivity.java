package com.ys.androidhook;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ys.androidhook.click.HookClickCommonActivity;
import com.ys.androidhook.toast.ToastWrapper;

public class MainActivity extends HookClickCommonActivity{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.text_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ToastWrapper.makeText(MainActivity.this, "点击按钮", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
