package org.github.palace.bot.core.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 插件lib类加载器
 * 用于加载maven打包的依赖关系, 不参与实际插件的加载，作为parent加载器。
 *
 * @author JHY
 * @date 2022/4/7 14:57
 */
public class PluginLibClassLoader extends URLClassLoader {

    private static URL[] urls = new URL[0];

    static {
        File file = new File(PluginRegister.PLUGIN_RESOURCE_LOCATION + "/lib");
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                URL[] urls = new URL[files.length];
                for (int i = 0; i < files.length; i++) {
                    try {
                        urls[i] = files[i].toURI().normalize().toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                PluginLibClassLoader.urls = urls;
            }
        }
    }

    public PluginLibClassLoader() {
        super(urls, Thread.currentThread().getContextClassLoader());
    }

}
