package org.github.palace.bot.core.cli.support;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import org.github.palace.bot.core.cli.CommandSender;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Answers.RETURNS_MOCKS;

/**
 * 参数解析器测试类
 *
 * @author jihongyuan
 * @date 2022/5/10 10:11
 */
public class ParameterResolverTest {

    public ParameterResolver commandSenderResolver = new ParameterResolver(Mockito.mock(MemberCommandSender.class, RETURNS_MOCKS));

    public ParameterResolver commandPusherResolver = new ParameterResolver(Mockito.mock(Bot.class, RETURNS_MOCKS));

    @Test
    public void testCommandSenderResolverParameter() {
        Class<?>[] parameterArgs = new Class[]{CommandSender.class};
        Object[] args = commandSenderResolver.resolver(parameterArgs);
        assertParameter(parameterArgs, args);
    }

    @Test
    public void testCommandSenderResolverParameters() {
        Class<?>[] parameterArgs = new Class[]{CommandSender.class, Bot.class, Contact.class, User.class, String.class};
        Object[] args = commandSenderResolver.resolver(parameterArgs);
        assertParameter(parameterArgs, args);
    }

    @Test
    public void testCommandSenderResolverParametersSort() {
        Class<?>[] parameterArgs = new Class[]{Bot.class, CommandSender.class, String.class, Contact.class, User.class,};
        Object[] args = commandSenderResolver.resolver(parameterArgs);
        assertParameter(parameterArgs, args);
    }

    @Test
    public void testCommandPusherResolverParameter() {
        Class<?>[] parameterArgs = new Class[]{Bot.class};
        Object[] args = commandPusherResolver.resolver(parameterArgs);
        assertParameter(parameterArgs, args);
    }

    public void assertParameter(Class<?>[] parameterArgs, Object[] args) {
        Assert.assertEquals(parameterArgs.length, args.length);
        for (int i = 0; i < parameterArgs.length; i++) {
            Assert.assertNotNull(args[i]);
            Assert.assertTrue(parameterArgs[i].isAssignableFrom(args[i].getClass()));
        }
    }
}
