package org.github.palace.bot.core.cli.support;

import org.github.palace.bot.core.cli.resolver.*;
import org.github.palace.bot.core.exception.ParameterResolverException;

import java.util.Set;

/**
 * @author JHY
 * @date 2022/4/2 16:58
 */
public class ParameterResolver {

    private final Object[] data;

    /**
     * 参数解析器
     */
    private static Set<Resolver> resolvers;

    static {
        resolvers = Set.of(new CommandSenderResolver(), new BotResolver(), new ContactResolver(), new UserResolver(), new StringResolver(), new CommandSessionResolver());
    }

    public ParameterResolver(Object... data) {
        this.data = data;
    }

    public Object[] resolver(Class<?>[] classes) {
        Object[] args = new Object[classes.length];

        for (int i = 0; i < classes.length; i++) {
            Resolver resolver;
            if ((resolver = getAndSupportResolver(classes[i])) == null) {
                throw new ParameterResolverException("not support resolver type " + classes[i]);
            }
            args[i] = resolver.resolveArgument(data);
        }

        return args;
    }

    private Resolver getAndSupportResolver(Class<?> parameterType) {
        for (Resolver resolver : resolvers) {
            if (resolver.supportParameter(parameterType)) {
                return resolver;
            }
        }
        return null;
    }

}
