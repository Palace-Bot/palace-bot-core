package org.github.palace.bot.core.cli.resolver;

import lombok.EqualsAndHashCode;
import org.github.palace.bot.core.cli.CommandSender;

/**
 * String, 解析发送者名称
 *
 * @author jihongyuan
 * @date 2022/5/9 16:52
 */

@EqualsAndHashCode
public class StringResolver implements Resolver {

    @Override
    public boolean supportParameter(Class<?> parameter) {
        return String.class == parameter;
    }

    @Override
    public <T> Object resolveArgument(T obj) {
        return obj instanceof CommandSender ? ((CommandSender) obj).getName() : null;
    }

}
