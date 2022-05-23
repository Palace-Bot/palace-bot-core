package org.github.palace.bot.core.plugin;

import net.mamoe.mirai.contact.MemberPermission;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author jihongyuan
 * @date 2022/5/18 10:05
 */
public class PluginTest {

    public Plugin plugin;

    @Before
    public void constructor() {
        plugin = new Plugin("hello-world-plugin", "1.0.0", "this hello world plugin");
    }

    @Test
    public void onLoad() {
        plugin.onLoad();
    }

    @Test
    public void onEnable() {
        plugin.onEnable();
    }

    @Test
    public void onDisable() {
        plugin.onDisable();
    }

    @Test
    public void register() {
        plugin.register(new AbstractCommand("hello", MemberPermission.MEMBER, false, "hello world") {
        });
        List<AbstractCommand> commands = plugin.getCommands();
        Assert.assertEquals(1, commands.size());
    }

}
