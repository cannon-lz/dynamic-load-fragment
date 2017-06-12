package com.zhangly.dynamic.plugin;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Created by zhangluya on 2017/6/9.
 */
class Plugin {

    private String path;
    private String name;
    PackageInfo info;
    private AssetManager assetManager;
    ClassLoader classLoader;
    private Resources resources;
    private Resources.Theme theme;

    public Plugin(String path, String name, AssetManager assetManager, ClassLoader classLoader, Resources resources, Resources.Theme theme, PackageInfo info) {
        this.path = path;
        this.name = name;
        this.assetManager = assetManager;
        this.classLoader = classLoader;
        this.resources = resources;
        this.theme = theme;
        this.info = info;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Resources getResources() {
        return resources;
    }

    public Resources.Theme getTheme() {
        return theme;
    }

    public PackageInfo getPackageInfo() {
        return info;
    }
}
