package org.github.palace.bot.core.loader;

import org.github.palace.bot.core.plugin.Plugin;

/**
 * @author jihongyuan
 * @date 2022/5/5 14:03
 */
public interface Loader {

    /**
     * @return the Java class loader to be used by this Container.
     */
    ClassLoader getClassLoader();

    /**
     * @return the Plugin with which this Loader has been associated.
     */
    Plugin getPlugin();

}
