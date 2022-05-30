package org.github.palace.bot.core.cli.support;

import net.mamoe.mirai.message.data.MessageSource;
import org.github.palace.bot.core.cli.CommandSession;
import org.github.palace.bot.core.plugin.AbstractCommand;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.test.HelperCommand;

import java.util.List;

import static org.mockito.Answers.RETURNS_MOCKS;

/**
 * @author jihongyuan
 * @date 2022/5/30 15:15
 */
public class CommandSessionHelperTest {
    public AbstractCommand command = new HelperCommand();
    public CommandSession commandSession = new CommandSession(command);
    public CommandSessionHelper helper = new CommandSessionHelper();

    @Test
    public void testPrepare() {
        CommandSession session = helper.prepare(commandSession);
        Assert.assertNotNull(session);
        Assert.assertEquals(CommandSession.State.PREPARE, session.getState());
    }

    @Test
    public void testCrash() {
        CommandSession session = helper.crash(commandSession);
        Assert.assertNotNull(session);
        Assert.assertEquals(CommandSession.State.CRASH, session.getState());
    }

    @Test
    public void testFinish() {
        CommandSession session = helper.finish(commandSession);
        Assert.assertNotNull(session);
        Assert.assertEquals(CommandSession.State.FINISH, session.getState());
    }

    @Test
    public void testPut() {
        MessageSource messageSource = Mockito.mock(MessageSource.class, RETURNS_MOCKS);
        CommandSession session = helper.put(messageSource, command);
        Assert.assertNotNull(session);
    }

    @Test
    public void testGet() {
        MessageSource messageSource = Mockito.mock(MessageSource.class, RETURNS_MOCKS);
        helper.put(messageSource, command);

        List<CommandSession> sessions = helper.get(messageSource, CommandSession.State.RUNNABLE);

        Assert.assertNotNull(sessions);
        Assert.assertTrue(sessions.size() > 0);
    }

    //TODO 批量测试 和 session生命周期测试
}
