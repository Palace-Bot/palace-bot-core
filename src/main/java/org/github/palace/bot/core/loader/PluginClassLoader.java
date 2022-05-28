package org.github.palace.bot.core.loader;

import lombok.extern.slf4j.Slf4j;

import java.net.*;

/**
 * 插件lib类加载器
 *
 * @author JHY
 * @date 2022/4/7 14:57
 */
@Slf4j
public class PluginClassLoader extends URLClassLoader {

    public PluginClassLoader(URL[] urls) {
        this(getSystemClassLoader(), urls);
    }

    public PluginClassLoader(ClassLoader parent, URL[] urls) {
        super(urls, parent);
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        // 使用parent查找
        if (className.startsWith("org.github.palace.bot") ||
                className.startsWith("java") ||
                className.startsWith("net.mamoe.mirai")) {
            return super.loadClass(className);
        }

        // 从当前类路径查找class
        synchronized (getClassLoadingLock(className)) {
            try {
                return this.findClass(className);
            } catch (ClassNotFoundException ignored) {
                // ignored
            }
        }


        // 使用parent查找
        return super.loadClass(className);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

}
