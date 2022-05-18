package org.github.palace.bot.core.utils;

import org.github.palace.bot.core.plugin.PluginProperties;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author jihongyuan
 * @date 2022/5/14 21:44
 */
public class PropertiesUtilTest {

    @Test
    public void getPropertiesKey() {
        Field[] propertiesKeys = PropertiesUtil.getPropertiesKeys(PluginProperties.class);

        Assert.assertNotNull(propertiesKeys);
        Assert.assertEquals(propertiesKeys.length, 3);
        Assert.assertEquals(propertiesKeys[0].getName(), "id");
        Assert.assertEquals(propertiesKeys[1].getName(), "mainClass");
        Assert.assertEquals(propertiesKeys[2].getName(), "version");

        // test cache
        propertiesKeys = PropertiesUtil.getPropertiesKeys(PluginProperties.class);
    }

}
