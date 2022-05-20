package org.github.palace.bot.core.cli.support;

import org.github.palace.bot.core.cli.resolver.*;
import org.github.palace.bot.core.exception.ParameterResolverException;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * @author JHY
 * @date 2022/4/2 16:58
 */
public class ParameterResolver {
    // TODO 待优化
    private final Object data;

    /**
     * 参数解析器
     */
    private static final Map<ResolverType, Set<Resolver>> resolvers = new EnumMap<>(ResolverType.class);

    static {
        resolvers.put(ResolverType.COMMAND_SENDER, Set.of(new CommandSenderResolver(), new BotResolver(), new ContactResolver(), new UserResolver(), new StringResolver()));
        // TODO 目前不支持MessageChina
        resolvers.put(ResolverType.MESSAGE_CHINA, null);
        // TODO 更多参数类型
        resolvers.put(ResolverType.COMMAND_PUSHER, Set.of(new BotResolver()));
    }

    public ParameterResolver(Object data) {
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
        for (Resolver resolver : resolvers.get(ResolverType.get(data.getClass()))) {
            if (resolver.supportParameter(parameterType)) {
                return resolver;
            }
        }
        return null;
    }

    @AllArgsConstructor
    @Getter
    public enum ResolverType {
        COMMAND_SENDER(CommandSender.class),
        COMMAND_PUSHER(Bot.class),
        MESSAGE_CHINA(MessageSource.class),
        ;
        private final Class<?> clazz;

        public static ResolverType get(Class<?> clazz) {
            for (ResolverType value : values()) {
                if (value.getClazz().isAssignableFrom(clazz)) {
                    return value;
                }
            }
            throw new ParameterResolverException("not support resolver type " + clazz);
        }
    }

}
