package org.github.palace.bot.core.plugin;

import org.github.palace.bot.core.loader.PluginClassLoader;
import org.junit.Before;

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

}
