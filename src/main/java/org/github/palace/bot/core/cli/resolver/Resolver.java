package org.github.palace.bot.core.cli.resolver;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MessageChain;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.cli.support.ParameterResolver;

/**
 * @author jihongyuan
 * @date 2022/5/9 16:28
 */
public interface Resolver {

    /**
     * Is support parameter
     *
     * @return true / false
     */
    boolean supportParameter(Class<?> parameter);

    /**
     * Resolver argument
     *
     * @param obj obj
     * @return resolver after data
     */
    default Object resolveArgument(Object obj) {
        Class<?> clazz = obj.getClass();
        if (ParameterResolver.ResolverType.COMMAND_SENDER.getClazz().isAssignableFrom(clazz)) {
            return resolveArgument((CommandSender) obj);
        } else if (ParameterResolver.ResolverType.MESSAGE_CHINA.getClazz().isAssignableFrom(clazz)) {
            return resolveArgument((MessageChain) obj);
        } else if (ParameterResolver.ResolverType.COMMAND_PUSHER.getClazz().isAssignableFrom(clazz)) {
            return resolveArgument((Bot) obj);
        }
        return null;
    }

    /**
     * Resolver argument
     *
     * @param commandSender 发送者信息
     * @return resolver after data
     */
    default Object resolveArgument(CommandSender commandSender) {
        return null;
    }

    /**
     * Resolver argument
     *
     * @param messageChain 消息链条
     * @return resolver after data
     */
    default Object resolveArgument(MessageChain messageChain) {
        return null;
    }

    /**
     * Resolver argument
     *
     * @param bot 机器人信息
     * @return resolver after data
     */
    default Object resolveArgument(Bot bot) {
        return null;
    }
}
