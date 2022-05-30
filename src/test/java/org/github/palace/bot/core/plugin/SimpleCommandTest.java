package org.github.palace.bot.core.plugin;

import net.mamoe.mirai.contact.MemberPermission;
import org.github.palace.bot.core.CommandScope;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author jihongyuan
 * @date 2022/5/30 15:46
 */
public class SimpleCommandTest {

    @Test
    public void constructor1() {
        SimpleCommand simpleCommand = new SimpleCommand("/simple", MemberPermission.MEMBER, "简单命令");
        Assert.assertNotNull(simpleCommand);
    }

    @Test
    public void constructor2() {
        SimpleCommand simpleCommand = new SimpleCommand("/simple", "简单命令");
        Assert.assertNotNull(simpleCommand);
    }

    @Test
    public void constructor3() {
        SimpleCommand simpleCommand = new SimpleCommand("/simple", MemberPermission.MEMBER, false, "简单命令");
        Assert.assertNotNull(simpleCommand);
    }

    @Test
    public void constructor4() {
        SimpleCommand simpleCommand = new SimpleCommand("/simple", MemberPermission.MEMBER, CommandScope.MEMBER, false, "简单命令");
        Assert.assertNotNull(simpleCommand);
    }

}
