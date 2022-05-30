package org.github.palace.bot.core.cli;


import org.junit.Assert;
import org.junit.Test;
import org.test.HelperCommand;

/**
 * @author jihongyuan
 * @date 2022/5/30 15:08
 */
public class CommandSessionTest {

    public CommandSession commandSession = new CommandSession(new HelperCommand());

    @Test
    public void testRunnable(){
        CommandSession session = commandSession.runnable();
        Assert.assertNotNull(session);
        Assert.assertEquals(CommandSession.State.RUNNABLE, session.getState());
    }

    @Test
    public void testPrepare(){
        CommandSession session = commandSession.prepare();
        Assert.assertNotNull(session);
        Assert.assertEquals(CommandSession.State.PREPARE, session.getState());
    }
    @Test
    public void testCrash(){
        CommandSession session = commandSession.crash();
        Assert.assertNotNull(session);
        Assert.assertEquals(CommandSession.State.CRASH, session.getState());
    }

    @Test
    public void testFinish(){
        CommandSession session = commandSession.finish();
        Assert.assertNotNull(session);
        Assert.assertEquals(CommandSession.State.FINISH, session.getState());
    }

    @Test
    public void testVisit(){
        long lastTimestamp = System.currentTimeMillis();
        CommandSession session = commandSession.runnable();
        Assert.assertTrue(session.getLastTimestamp() >= lastTimestamp);
    }

}
