package org.github.palace.bot.core.plugin;

/**
 * 插件生命周期
 *
 * @author JHY
 * @date 2022/3/30 16:14
 */
public interface PluginLoaderLifeCycle {

    /**
     * 加载
     */
    void onLoad();

    /**
     * 启用
     */
    void onEnable();

    /**
     * 禁用
     */
    void onDisable();

}
