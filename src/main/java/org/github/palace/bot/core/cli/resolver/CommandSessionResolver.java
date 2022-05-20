package org.github.palace.bot.core.cli.resolver;

import lombok.EqualsAndHashCode;
import org.github.palace.bot.core.cli.CommandSession;

/**
 * @author jihongyuan
 * @date 2022/5/19 15:25
 */
@EqualsAndHashCode
public class CommandSessionResolver implements Resolver {

    @Override
    public boolean supportParameter(Class<?> parameter) {
        return CommandSession.class == parameter;
    }

    @Override
    public <T> Object resolveArgument(T obj) {
        return obj instanceof CommandSession ? obj : null;
    }

}
