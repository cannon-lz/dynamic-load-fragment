package com.zhangly.dynamic.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v4.util.ArrayMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by zhangluya on 2017/6/9.
 */

public class PluginManager {

    private static volatile PluginManager sPluginManager;

    private ArrayMap<String, Plugin> mInstalled = new ArrayMap<>();

    private PluginManager() {
    }

    public static PluginManager getInstance() {
        if (sPluginManager == null) {
            synchronized (PluginManager.class) {
                if (sPluginManager == null) {
                    sPluginManager = new PluginManager();
                }
            }
        }
        return sPluginManager;
    }

    public Plugin install(Context context, String pluginPath) {
        context = context.getApplicationContext();
        PackageInfo packageInfo = getPackageInfo(context, pluginPath);
        String pluginName = packageInfo.applicationInfo.metaData.getString("name");
        Plugin plugin = mInstalled.get(pluginName);
        if (plugin != null) {
            return plugin;
        }
        String optimizedDirectory = context.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath();
        DexClassLoader classLoader = new DexClassLoader(pluginPath, optimizedDirectory, null, context.getClassLoader());
        try {
            Class<AssetManager> assetManagerClass = AssetManager.class;
            AssetManager assetManager = assetManagerClass.newInstance();
            Method addAssetPath = assetManagerClass.getMethod("addAssetPath", String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(assetManager, pluginPath);

            Resources superResource = context.getResources();
            Resources res = new Resources(assetManager, superResource.getDisplayMetrics(), superResource.getConfiguration());
            Resources.Theme theme = res.newTheme();
            theme.setTo(context.getTheme());

            Plugin p = new Plugin(pluginPath, pluginName, assetManager, classLoader, res, theme, packageInfo);
            mInstalled.put(pluginName, p);
            return p;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath,
                    PackageManager.GET_ACTIVITIES |
                            PackageManager.GET_SERVICES |
                            PackageManager.GET_META_DATA);
        } catch (Exception e) {
            // should be something wrong with parse
            e.printStackTrace();
        }
        return pkgInfo;
    }
}
