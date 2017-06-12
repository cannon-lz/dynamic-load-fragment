package com.zhangly.dynamic.plugin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.zhangly.dynamic.R;

/**
 * Created by zhangluya on 2017/6/9.
 */

public class PluginHostActivity extends AppCompatActivity {

    private Plugin mPlugin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(android.R.id.primary);
        setContentView(frameLayout);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        mPlugin = PluginManager.getInstance().install(this, path);

        if (mPlugin != null) {

            try {
                Bundle metaData = mPlugin.info.applicationInfo.metaData;
                String rootFragment = metaData.getString("mainFragment");
                int version = metaData.getInt("version");
                String name = metaData.getString("name");

                Log.i(PluginHostActivity.class.getName(), String.format("mainFragment %s, version %s, name %s", rootFragment, version, name));
                Class<?> mainFragmentClass = mPlugin.classLoader.loadClass(rootFragment);
                Fragment mainFragment = (Fragment) mainFragmentClass.newInstance();
                getSupportFragmentManager().beginTransaction().add(android.R.id.primary, mainFragment).commit();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public AssetManager getAssets() {
        return mPlugin == null ? super.getAssets() : mPlugin.getAssetManager();
    }

    @Override
    public Resources getResources() {
        return mPlugin == null ? super.getResources() : mPlugin.getResources();
    }

    /*@Override
    public Resources.Theme getTheme() {
        return mPlugin == null ? super.getTheme() : mPlugin.getTheme();
    }*/

    @Override
    public ClassLoader getClassLoader() {
        return mPlugin == null ? super.getClassLoader() : mPlugin.getClassLoader();
    }
}
