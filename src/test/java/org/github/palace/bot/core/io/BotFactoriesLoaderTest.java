package org.github.palace.bot.core.io;

import org.github.palace.bot.core.EventDispatcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author jihongyuan
 * @date 2022/5/30 15:41
 */
public class BotFactoriesLoaderTest {

    @Test
    public void testLoadFactoryNames() {
        List<String> handlerNames = BotFactoriesLoader.loadFactoryNames(EventDispatcher.class, BotFactoriesLoaderTest.class.getClassLoader());
        Assert.assertNotNull(handlerNames);
        Assert.assertEquals(3, handlerNames.size());
    }
}
