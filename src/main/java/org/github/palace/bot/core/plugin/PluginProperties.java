package org.github.palace.bot.core.plugin;

import lombok.extern.slf4j.Slf4j;
import org.github.palace.bot.core.prop.AbstractProperties;

/**
 * @author jihongyuan
 * @date 2022/5/14 21:14
 */
@Slf4j
public class PluginProperties extends AbstractProperties {
    public static final String DEFAULT_PROPERTIES_FILE_NAME = "plugin.properties";

    private static final String DEFAULT_PROPERTIES_PLUGIN_PREFIX = "plugin";

    public String id;

    public String mainClass;

    public String version;

    public PluginProperties() {
        super(DEFAULT_PROPERTIES_FILE_NAME);
    }

    /**
     * @param path plugin.properties file path
     */
    public PluginProperties(String path) {
        super(path);
    }

    /**
     * @param classLoader use {@code classloader.getResourceAsStream()}
     */
    public PluginProperties(ClassLoader classLoader) {
        super(DEFAULT_PROPERTIES_FILE_NAME, classLoader);
    }

    @Override
    public String getPropertiesPrefix() {
        return DEFAULT_PROPERTIES_PLUGIN_PREFIX;
    }

    @Override
    public String toString() {
        return "PluginProperties{" +
                "id='" + id + '\'' +
                ", mainClass='" + mainClass + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

}
