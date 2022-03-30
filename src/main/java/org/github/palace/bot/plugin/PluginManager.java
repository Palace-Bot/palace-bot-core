package org.github.palace.bot.plugin;

import org.github.palace.bot.core.exception.PluginException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JHY
 * @date 2022/3/30 16:16
 */
public class PluginManager {

    /**
     * The location to look plugin JAR
     */
    public static final String PLUGIN_RESOURCE_LOCATION = "plugin";


    /**
     * plugin目录下，为每一个jar创建类加载器
     * key: plugin name + ":" +  plugin version, value: classloader
     */
    private Map<String, PluginClassLoader> pluginIdClassLoaderMap = new HashMap<>(16);


    public PluginManager() {
        List<PluginClassLoader> pluginLoaders = createPluginLoaders(PLUGIN_RESOURCE_LOCATION);
        for (PluginClassLoader pluginLoader : pluginLoaders) {
            try {
                Class<?> mainClass = pluginLoader.loadMainClass();
                Object o = mainClass.newInstance();

                if (o instanceof Plugin) {
                    Plugin plugin = (Plugin) o;
                    System.out.println(plugin.getName());
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private List<PluginClassLoader> createPluginLoaders(String location) {
        List<PluginClassLoader> pluginLoaderList = new ArrayList<>();
        File file = new File(location);
        if (file.canRead() && file.isDirectory()) {
            File[] files = file.listFiles();

            if (files == null || files.length == 0) {
                return pluginLoaderList;
            }

            for (File item : files) {
                if (item.getName().endsWith(".jar")) {
                    try {
                        pluginLoaderList.add(new PluginClassLoader(item));
                    } catch (Exception e) {
                        throw new PluginException("plugin load error: " + item.getName(), e);
                    }
                }
            }

        }
        return pluginLoaderList;
    }

}
