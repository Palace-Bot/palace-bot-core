package org.github.palace.bot.core.loader;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Objects;

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
        if (className.startsWith("jdk") || className.startsWith("java") || className.startsWith("com.sun") || className.startsWith("org.xml")) {
            return findSystemClass(className);
        }

        if (className.startsWith("org.github.palace.bot.core") || className.startsWith("org.github.palace.bot.app") ||
                className.startsWith("org.github.palace.bot.data") || className.startsWith("net.mamoe.mirai")) {
            return getParent().loadClass(className);
        }

        // second check whether it's already been loaded
        Class<?> loadedClass = findLoadedClass(className);
        if (loadedClass != null) {
            log.trace("Found loaded class '{}'", className);
            return loadedClass;
        }

        synchronized (getClassLoadingLock(className)) {
            try {
                return findClass(className);
            } catch (ClassNotFoundException ignored) {
                // ignored
            }
        }

        throw new ClassNotFoundException(className);
    }


    public URL getResource(String name) {
        Objects.requireNonNull(name);
        URL url = findResource(name);

        if (url == null) {
            return super.getResource(name);
        }
        return url;
    }

}
