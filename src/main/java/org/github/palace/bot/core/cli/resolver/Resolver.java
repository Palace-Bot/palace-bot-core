package org.github.palace.bot.core.cli.resolver;


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
     * @param objs objs
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
        return result;
    }

    <T> Object resolveArgument(T obj);

}
