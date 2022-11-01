package org.github.palace.bot.core.plugin;

import org.github.palace.bot.core.loader.PluginClassLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

/**
 * @author jihongyuan
 * @date 2022/5/18 10:13
 */
public class PluginWrapperTest {

    public PluginWrapper pluginWrapper;

    @Before
    public void constructor() {
        PluginProperties pluginProperties = new PluginProperties();
        // TODO
        pluginWrapper = new PluginWrapper(pluginProperties, null, new PluginClassLoader(new URL[]{}), null);
    }

    @Test
    public void createPlugin() {
        pluginWrapper.createPlugin();

        Plugin plugin = pluginWrapper.getPlugin();
        Assert.assertNotNull(plugin);
    }
}
