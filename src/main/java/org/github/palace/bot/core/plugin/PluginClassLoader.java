package org.github.palace.bot.core.plugin;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * 插件式类加载器
 * 一个jar包一个加载器做隔离
 *
 * @author JHY
 * @date 2022/3/30 16:07
 */
@EqualsAndHashCode
public final class PluginClassLoader implements PluginLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginClassLoader.class);

    /**
     * 插件主类名称
     */
    private String mainClass;
    private final URLClassLoader classLoader;

    public PluginClassLoader(String path) {
        this(new File(path), null);
    }

    public PluginClassLoader(File path) {
        this(path, null);
    }

    public PluginClassLoader(File path, @Nullable ClassLoader parent) {
        this.classLoader = createClassLoader(path, parent);
    }

    public Class<?> loadMainClass() throws ClassNotFoundException {
        return this.loadClass(mainClass);
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return classLoader.loadClass(className);
    }

    /**
     * 创建一个类加载器
     *
     * @param file   jar
     * @param parent classloader
     */
    private URLClassLoader createClassLoader(final File file, @Nullable ClassLoader parent) {
        if (parent == null) {
            parent = Thread.currentThread().getContextClassLoader();
        }
        return replaceClassLoader(URLClassLoader.newInstance(new URL[0], parent), file);
    }

    /**
     * 替换旧的类加载器
     *
     * @param oldLoader 旧的类加载器
     * @param file      jar包
     * @return 包含旧的类加载器，返回新的加载器
     */
    private URLClassLoader replaceClassLoader(final URLClassLoader oldLoader, final File file) {
        if (file != null && file.canRead() && file.isFile()) {
            URL[] elements = new URL[1];

            try (JarFile jarFile = new JarFile(file)) {
                Manifest manifest = jarFile.getManifest();
                Attributes attributes = manifest.getMainAttributes();
                mainClass = attributes.getValue("Main-Class");

                URL element = file.toURI().normalize().toURL();
                elements[0] = element;

                ClassLoader oldParent = oldLoader.getParent();
                return URLClassLoader.newInstance(elements, oldParent);
            } catch (IOException ignored) {
            }
        }

        return oldLoader;
    }

    @Nullable
    @Override
    public Plugin load() {
        try {
            Class<?> clazz = this.loadMainClass();
            Object obj = clazz.getDeclaredConstructor().newInstance();
            if (obj instanceof Plugin) {
                return (Plugin) obj;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}