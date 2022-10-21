package org.github.palace.bot.core.util;

import java.net.URL;

/**
 * @author jihongyuan
 * @date 2022/9/9 15:49
 */
public class ResourceUtils {

    public static final String PLUGINS_URL = "plugins";

    /** URL prefix for loading from the file system: "file:". */
    public static final String FILE_URL_PREFIX = "file";

    /** URL prefix for loading from a jar file: "jar:". */
    public static final String JAR_URL_PREFIX = "jar:";

    public static final String URL_PROTOCOL_JAR = "jar";

    /** URL protocol for an entry from a war file: "war". */
    public static final String URL_PROTOCOL_WAR = "war";

    /** URL protocol for an entry from a zip file: "zip". */
    public static final String URL_PROTOCOL_ZIP = "zip";

    public static boolean isJarURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_WAR.equals(protocol) || URL_PROTOCOL_ZIP.equals(protocol));
    }

}
