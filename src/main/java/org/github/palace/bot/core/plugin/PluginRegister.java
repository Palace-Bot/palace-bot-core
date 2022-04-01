package org.github.palace.bot.core.plugin;

import lombok.Getter;
import org.github.palace.bot.core.exception.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取指定目录下jar并注册进来
 *
 * @author JHY
 * @date 2022/3/30 16:16
 */
public class PluginRegister {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginRegister.class);

    /**
     * The location to look plugin JAR
     */
    public static final String PLUGIN_RESOURCE_LOCATION = "plugin";

    /**
     * key: classloader, value: plugin
     */
    @Getter
    private final Map<PluginLoader, Plugin> pluginCache = new HashMap<>(16);

    public PluginRegister() {
        List<PluginLoader> pluginLoaders = createPluginLoaders(PLUGIN_RESOURCE_LOCATION);
        for (PluginLoader pluginLoader : pluginLoaders) {
            Plugin plugin = pluginLoader.load();
            if (plugin != null) {
                pluginCache.put(pluginLoader, plugin);
            }
        }
    }

    public void init() {
        pluginCache.forEach((k, v) -> {
            // 注册command
            v.onLoad();
            // 启动插件
            v.onDisable();
        });
    }

    private List<PluginLoader> createPluginLoaders(String location) {
        List<PluginLoader> pluginLoaderList = new ArrayList<>();
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
