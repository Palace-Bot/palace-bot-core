package org.github.palace.bot.core.cli.resolver;

import lombok.EqualsAndHashCode;
import org.github.palace.bot.core.cli.CommandSender;

/**
 * @author jihongyuan
 * @date 2022/5/9 16:29
 */

@EqualsAndHashCode
public class CommandSenderResolver implements Resolver {

    @Override
    public boolean supportParameter(Class<?> parameter) {
        return CommandSender.class == parameter;
    }

    @Override
    public CommandSender resolveArgument(CommandSender commandSender) {
        return commandSender;
    }

}
