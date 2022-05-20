package org.github.palace.bot.core.prop;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author jihongyuan
 * @date 2022/5/15 13:52
 */
public class AbstractPropertiesTest {

    @Test
    public void constructor() {
        AbstractProperties pluginProperties = new AbstractProperties("plugin.properties") {
            public String id;
            public String mainClass;
            public String version;

            @Override
            public String getPropertiesPrefix() {
                return "plugin";
            }

            @Override
            public String toString() {
                return "id=" + id + "&mainClass=" + mainClass + "&version=" + version;
            }
        };

        Assert.assertNotNull(pluginProperties);

        String str = pluginProperties.toString();
        Assert.assertEquals(str, "id=hello-world&mainClass=org.test.App&version=1.0-SNAPSHOT");
    }
}
