package org.github.palace.bot.core.plugin;

import org.github.palace.bot.core.annotation.CommandHandler;
import org.github.palace.bot.core.annotation.CommandPusher;
import org.github.palace.bot.core.plugin.AbstractCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.test.HelperCommand;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author jihongyuan
 * @date 2022/5/18 10:32
 */
public class AbstractCommandTest {
    public AbstractCommand command;

    @Before
    public void constructor() {
        command = new HelperCommand();
    }

    @Test
    public void parse() {
        command.parse();
        Map<Method, CommandHandler> commandHandlerMethodMap = command.getCommandHandlerMethodMap();

        Assert.assertNotNull(commandHandlerMethodMap);
        Assert.assertEquals(1, commandHandlerMethodMap.size());

        Map<Method, CommandPusher> commandPusherMethodMap = command.getCommandPusherMethodMap();
        Assert.assertNotNull(commandPusherMethodMap);
        Assert.assertEquals(1, commandPusherMethodMap.size());

        List<AbstractCommand> childrenCommand = command.getChildrenCommand();
        Assert.assertNotNull(childrenCommand);
        Assert.assertEquals(1, childrenCommand.size());
    }

}
