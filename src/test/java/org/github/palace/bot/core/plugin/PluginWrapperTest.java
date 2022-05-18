package org.github.palace.bot.core.plugin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jihongyuan
 * @date 2022/5/18 10:13
 */
public class PluginWrapperTest {

    public PluginWrapper pluginWrapper;

    @Before
    public void constructor() {
        PluginProperties pluginProperties = new PluginProperties();
        pluginWrapper = new PluginWrapper(pluginProperties, Thread.currentThread().getContextClassLoader());
    }

    @Test
    public void createPlugin() {
        pluginWrapper.createPlugin();

        Plugin plugin = pluginWrapper.getPlugin();
        Assert.assertNotNull(plugin);
    }
}
