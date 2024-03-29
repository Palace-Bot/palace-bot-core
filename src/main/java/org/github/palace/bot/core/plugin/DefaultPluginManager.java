package org.github.palace.bot.core.plugin;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;

/**
 * @author jihongyuan
 * @date 2022/5/17 10:38
 */
@Slf4j
public class DefaultPluginManager extends AbstractPluginManager {

    public DefaultPluginManager(Bot bot, String pluginPath) {
        super(bot, pluginPath);
    }

    /**
     * start
     */
    @Override
    public void start() {
        bot.login();

        for (PluginWrapper pluginWrapper : plugins) {
            Plugin plugin = null;
            try {
                plugin = pluginWrapper.createPlugin();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            if (plugin != null) {
                long start = System.currentTimeMillis();
                try {
                    plugin.onLoad();
                    pluginWrapper.start();
                    log.info("start plugin: {}, time: {}ms", pluginWrapper.getPlugin().getName(), System.currentTimeMillis() - start);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void stop() {
        for (PluginWrapper pluginWrapper : plugins) {
            pluginWrapper.stop();
        }
    }

}
