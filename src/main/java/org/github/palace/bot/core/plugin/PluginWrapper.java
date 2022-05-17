package org.github.palace.bot.core.plugin;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.github.palace.bot.core.cli.AbstractCommand;

import java.util.Set;

/**
 * 插件包装器
 *
 * @author jihongyuan
 * @date 2022/5/12 16:48
 */
@Getter
@Slf4j
public class PluginWrapper {

    /**
     * plugin properties
     */
    private final PluginProperties properties;

    private final ClassLoader pluginLoader;

    @Getter
    private Plugin plugin;

    public PluginWrapper(PluginProperties properties, ClassLoader pluginLoader) {
        this.properties = properties;
        this.pluginLoader = pluginLoader;
    }

    /**
     * Create Plugin
     */
    public void createPlugin() {
        Plugin plugin = null;
        try {
            Class<?> clazz = pluginLoader.loadClass(properties.mainClass);
            plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        this.plugin = plugin;
    }

    public Set<AbstractCommand> getCommands() {
        return plugin.getCommands();
    }
}
