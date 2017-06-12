package com.zhangly.dynamic.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhangly.dynamic.R;

import java.io.File;

/**
 * Created by zhangluya on 2017/6/12.
 */

public class MainActivity extends AppCompatActivity {

    private static final String PLUGIN_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "myPlugin" + File.separator + "plugin1-debug.apk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startPluginOne(View view) {
        Intent intent = new Intent("plugin.intent.launcher");
        intent.putExtra("path", PLUGIN_PATH);
        startActivity(intent);
    }
}
