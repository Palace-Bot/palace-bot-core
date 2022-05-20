package org.github.palace.bot.core.loader;

import lombok.extern.slf4j.Slf4j;
import org.github.palace.bot.core.plugin.PluginLoader;
import org.github.palace.bot.utils.ZipUtil;

import java.net.*;
import java.util.*;

/**
 * 插件lib类加载器
 *
 * @author JHY
 * @date 2022/4/7 14:57
 */
@Slf4j
public class PluginClassLoader extends URLClassLoader implements PluginLoader {


    /**
     * Public cache repositories
     */
    private final List<URL> repositories = new ArrayList<>();

    /**
     * The parent class loader.
     */
    protected final ClassLoader parent;

    /**
     * @param path parent plugin path
     */
    public PluginClassLoader(String path) {
        this(path, null);
    }

    /**
     * @param path   parent plugin path
     * @param parent parent classloader
     */
    public PluginClassLoader(String path, ClassLoader parent) {
        super(new URL[0], parent);

        ClassLoader p = getParent();
        if (p == null) {
            p = getSystemClassLoader();
        }

        this.parent = p;

        // 初始化仓库
        repositories.addAll(Arrays.asList(ZipUtil.getResources(path)));
        repositories.addAll(Arrays.asList(ZipUtil.getResources(path + "/lib")));
        for (URL repository : repositories) {
            super.addURL(repository);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        if (className.startsWith("org.github.palace.bot.core") ||
                className.startsWith("org.github.palace.bot.data") ||
                className.startsWith("java") ||
                className.startsWith("net.mamoe.mirai")) {
            return parent.loadClass(className);
        }

        Class<?> clazz = null;
        try {
            clazz = findClass(className);
        } catch (ClassNotFoundException e) {
        }
        if (clazz != null) {
            return clazz;
        }

        return parent.loadClass(className);
    }

}
