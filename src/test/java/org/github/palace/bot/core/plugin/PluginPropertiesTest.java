package org.github.palace.bot.core.plugin;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author jihongyuan
 * @date 2022/5/14 22:01
 */
public class PluginPropertiesTest {

    @Test
    public void constructor() {
        PluginProperties pluginProperties = new PluginProperties();
        validate(pluginProperties);
    }

    @Test
    public void constructorPath() {
        PluginProperties pluginProperties = new PluginProperties("plugin.properties");
        validate(pluginProperties);
    }

    @Test
    public void constructorClassLoader() {
        PluginProperties pluginProperties = new PluginProperties(Thread.currentThread().getContextClassLoader());
        validate(pluginProperties);
    }

    public void validate(PluginProperties pluginProperties) {
        Assert.assertNotNull(pluginProperties);
        Assert.assertNotNull(pluginProperties.id);
        Assert.assertNotNull(pluginProperties.mainClass);
        Assert.assertNotNull(pluginProperties.version);

        Assert.assertEquals(pluginProperties.id, "hello-world");
        Assert.assertEquals(pluginProperties.mainClass, "org.github.word.cloud.App");
        Assert.assertEquals(pluginProperties.version, "1.0-SNAPSHOT");
    }
}
