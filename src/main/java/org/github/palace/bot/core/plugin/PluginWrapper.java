package org.github.palace.bot.core.plugin;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.github.palace.bot.core.LifeCycle;
import org.github.palace.bot.core.annotation.Application;
import org.github.palace.bot.core.exception.PluginException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 插件包装器
 *
 * @author jihongyuan
 * @date 2022/5/12 16:48
 */
@Getter
@Slf4j
public class PluginWrapper implements LifeCycle {

    /**
     * plugin properties
     */
    private final PluginProperties properties;

    private final ClassLoader pluginLoader;

    private final CommandManager commandManager;

    @Getter
    private final ScheduledExecutorService pusherExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Getter
    private Plugin plugin;

    public PluginWrapper(PluginProperties properties, ClassLoader pluginLoader) {
        this.properties = properties;
        this.pluginLoader = pluginLoader;
        this.commandManager = new CommandManager(properties.commandPrefix, this);
    }

    /**
     * Create Plugin
     */
    public void createPlugin() {
        Plugin plugin = null;
        try {
            Class<?> clazz = pluginLoader.loadClass(properties.mainClass);
            if (clazz.isAnnotationPresent(Application.class)) {
                plugin = new PluginDelegate(clazz.getAnnotation(Application.class), clazz);
            } else if (clazz.isAssignableFrom(Plugin.class)) {
                plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
            } else {
                throw new PluginException("Plugin main class must extends from Plugin or annotated with @Application");
            }
            plugin.onLoad();
//            onLoadInternal();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        this.plugin = plugin;
    }

    @Override
    public void start() {
        for (AbstractCommand command : plugin.getCommands()) {
            commandManager.addCommand(command);
        }
        commandManager.start();
    }

    @Override
    public void stop() {
        pusherExecutorService.shutdown();
        commandManager.stop();
    }

}
