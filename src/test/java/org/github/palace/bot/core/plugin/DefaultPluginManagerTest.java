package org.github.palace.bot.core.plugin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

/**
 * @author jihongyuan
 * @date 2022/5/30 15:49
 */
public class DefaultPluginManagerTest {
    public static final String PLUGIN_NAME = "example-1.0-SNAPSHOT";

    public DefaultPluginManager pluginManager;

    @Before
    public void constructor() {
        pluginManager = new DefaultPluginManager(null, Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath() + "plugins");
    }

    @After
    public void after() throws IOException {
        pluginManager.stop();
        pluginManager = null;
        System.gc();
        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath() + "plugins" + PLUGIN_NAME;
        File file = new File(path);
        FileUtils.deleteDirectory(file);
    }

    // TODO
    @Test
    public void testLoad() {
        pluginManager.load();
    }

    @Test
    public void testStart() {
        pluginManager.load();
        pluginManager.start();

        List<PluginWrapper> plugins = pluginManager.plugins;
        System.out.println();
    }
}
